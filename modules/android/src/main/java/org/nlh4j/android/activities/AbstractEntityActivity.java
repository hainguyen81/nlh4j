/*
 * @(#)AbstractEntityActivity.java 1.0 Oct 27, 2016
 * Copyright 2016 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.android.activities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.nlh4j.android.annotation.ResourceElement;
import org.nlh4j.android.db.AbstractEntity;
import org.nlh4j.android.db.SqlLiteEntityDao;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.android.util.MetricUtils;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;
import org.springframework.util.Assert;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Activity with the specified entity to generate dynamic layout
 *
 * @param <T> the entity type
 *
 * @author Hai Nguyen
 *
 */
@SuppressWarnings("unchecked")
public abstract class AbstractEntityActivity<T extends AbstractEntity> extends AbstractActivity {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    protected static final float DEFAULT_LABEL_RATIO = 0.3f;
    protected static final int DEFAULT_FIELDS_DISTANCE = 5;

    /** entity class */
    private Class<T> entityClass;
    /** entity data */
    private T entity;
    /** whole field layouts */
    private transient Map<String, Map<String, View>> groups;
    /** whole field layouts */
    private transient Map<String, View> layouts;
    /** whole data field layout */
    private transient Map<String, View> fields;
    /** whole data field caption layout */
    private transient Map<String, View> labels;
    /** save action */
    private transient Button saveBtn;
    /** reset action */
    private transient Button resetBtn;
    /** {@link SqlLiteEntityDao} */
    private SqlLiteEntityDao<T> entityDao;

    /**
     * Initialize a new instance of {@link AbstractEntityActivity}
     *
     * @param entityClass the entity class to generate layout elements
     */
    protected AbstractEntityActivity(Class<T> entityClass) {
        Assert.notNull(entityClass, "entityClass");
        this.entityClass = entityClass;
    }
    /**
     * Initialize a new instance of {@link AbstractEntityActivity}
     *
     * @param entityClass the entity class to generate layout elements
     */
    protected AbstractEntityActivity(T entity) {
        Assert.notNull(entity, "entity");
        this.entity = entity;
        this.entityClass = (Class<T>) entity.getClass();
    }

    /**
     * Initialization GUI
     * @param entityClass the entity class to generate layout elements
     */
    private void initGUI() {
        // parse fields that has resource annotation
        Map<Field, ResourceElement> fields = BeanUtils.getMapFieldsBy(this.entityClass, ResourceElement.class);
        Assert.notEmpty(fields, "Not found any field with " + ResourceElement.class.getName() + " annotation");

        // parse field to create element
        Map<Integer, List<String>> sortedFields = new HashMap<Integer, List<String>>();
        Map<View, Integer> groupSortedFields = new HashMap<View, Integer>();
        this.groups = new LinkedHashMap<String, Map<String, View>>();
        this.layouts = new LinkedHashMap<String, View>();
        this.fields = new LinkedHashMap<String, View>();
        this.labels = new LinkedHashMap<String, View>();
        if (!CollectionUtils.isEmpty(fields)) {
            double w = MetricUtils.ratioToWidthPixels(this, DEFAULT_LABEL_RATIO);
            for(final Iterator<Field> it = fields.keySet().iterator(); it.hasNext();) {
                // parse resource annotation
                Field field = it.next();
                ResourceElement res = fields.get(field);
                if (res == null) continue;
                String fieldName = field.getName();

                // create field layout for label and editable text field
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0f);
                layoutParams.gravity = Gravity.LEFT | Gravity.FILL_HORIZONTAL;
                layoutParams.topMargin = DEFAULT_FIELDS_DISTANCE;
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(
                        this.isHorizontal()
                        ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
                layout.setTag(fieldName);
                layout.setId((fieldName + "_layout").hashCode());
                layout.setLayoutParams(layoutParams);

                // create field label
                if (res.value() > 0 && res.caption()) {
                    LinearLayout.LayoutParams lblParams = new LinearLayout.LayoutParams(
                            (int) w, LayoutParams.WRAP_CONTENT);
                    lblParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                    TextView lbl = new TextView(this);
                    lbl.setText(res.value());
                    lbl.setTag(fieldName);
                    lbl.setLayoutParams(lblParams);
                    lbl.setId((fieldName + "_label").hashCode());
                    layout.addView(lbl);
                    this.labels.put(fieldName, lbl);
                }

                // create text field
                LinearLayout.LayoutParams fldParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (float) (1 - DEFAULT_LABEL_RATIO));
                fldParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                View fieldView = null;
                if (res.component() != null) {
                    // create by annotation
                    LogUtils.d("Create component {0} for field {1} by newInstance",
                            new Object[] { res.component().getName(), fieldName });
                    fieldView = BeanUtils.safeNewInstance(
                            res.component(), new Object[] { this });
                }
                // create field component by internal method
                if (fieldView == null) {
                    LogUtils.d("Create component {0} for field {1} by user-defined",
                            new Object[] { res.component().getName(), fieldName });
                    fieldView = this.createFieldComponent(
                            res.value(), fieldName, field.getType());
                }
                // default field component
                if (fieldView == null) {
                    LogUtils.d("Create default EditText component for field {0}", new Object[] { fieldName });
                    fieldView = new EditText(this);
                }
                fieldView.setTag(fieldName);
                fieldView.setLayoutParams(fldParams);
                fieldView.setId((fieldName + "_editor").hashCode());
                // custom field view
                this.customFieldView(fieldName, fieldView);
                // need to attach field value
                if (this.entity != null) {
                    this.attachFieldData(fieldName, fieldView,
                            BeanUtils.getFieldValue(this.entity, fieldName));
                }
                layout.addView(fieldView);

                // field sort orders
                int order = Math.max(res.order(), 0);
                if (!sortedFields.containsKey(order)) {
                    sortedFields.put(order, new LinkedList<String>());
                }

                // caches field component
                this.fields.put(fieldName, fieldView);
                if (!StringUtils.hasText(res.group())) {
                    this.layouts.put(fieldName, layout);
                    sortedFields.get(order).add(fieldName);
                } else {
                    ViewGroup group = BeanUtils.safeType(this.layouts.get(res.group()), ViewGroup.class);
                    // if new group
                    if (group == null) {
                        LinearLayout.LayoutParams grpParams = new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0f);
                        grpParams.gravity = Gravity.LEFT | Gravity.FILL_HORIZONTAL;
                        grpParams.topMargin = DEFAULT_FIELDS_DISTANCE;
                        group = new LinearLayout(this);
                        ((LinearLayout) group).setOrientation(LinearLayout.VERTICAL);
                        group.setTag(order);
                        group.setLayoutParams(grpParams);
                        group.setId((res.group() + "_group").hashCode());
                        this.layouts.put(res.group(), group);
                        group.addView(layout);
                        sortedFields.get(order).add(res.group());

                        // sort current group
                    } else {
                        int idx = group.getChildCount();
                        for(int i = 0; i < group.getChildCount(); i++) {
                            View child = group.getChildAt(i);
                            int childOrder = (groupSortedFields.containsKey(child) ? groupSortedFields.get(child) : -1);
                            if (order <= childOrder) {
                                idx = i;
                                break;
                            }
                        }
                        group.addView(layout, idx);

                        // check group order in root layout
                        int grpOrder = NumberUtils.toInt(String.valueOf(group.getTag()), 0);
                        if (grpOrder > order) {
                            // cache minimum order
                            group.setTag(order);
                            // remove old order sorter
                            sortedFields.get(grpOrder).remove(res.group());
                            // add new minimum order sorter
                            sortedFields.get(order).add(res.group());
                        }
                    }
                    // sort group
                    groupSortedFields.put(layout, res.order());
                    if (!this.groups.containsKey(res.group())) {
                        this.groups.put(res.group(), new LinkedHashMap<String, View>());
                    }
                    this.groups.get(res.group()).put(fieldName, layout);
                }
            }

            // sort by order
            if (!CollectionUtils.isEmpty(this.layouts)) {
                // check ordered fields
                Map<String, View> sortedMap = new LinkedHashMap<String, View>();
                sortedFields = new TreeMap<Integer, List<String>>(sortedFields);
                for(final Iterator<Integer> it = sortedFields.keySet().iterator(); it.hasNext();) {
                    Integer order = it.next();
                    List<String> fieldNames = sortedFields.get(order);
                    if (CollectionUtils.isEmpty(fieldNames)) continue;
                    for(String fieldName : fieldNames) {
                        if (this.layouts.containsKey(fieldName)) {
                            sortedMap.put(fieldName, this.layouts.get(fieldName));
                        }
                    }
                }
                // check fields that has no order
                for(final Iterator<String> it = this.layouts.keySet().iterator(); it.hasNext();) {
                    String fieldName = it.next();
                    if (sortedMap.containsKey(fieldName)) continue;
                    sortedMap.put(fieldName, this.layouts.get(fieldName));
                }
                this.layouts = new LinkedHashMap<String, View>(sortedMap);
            }
        }

        // check valid entity
        Assert.notEmpty(this.layouts, "Not found any field that has been declared " + ResourceElement.class.getName() + " annotation!");
    }
    /**
     * Show GUI
     */
    private void showGUI() {
        // add field components to main layout
        ViewGroup view = this.findFieldsLayout();
        boolean fieldsLayout = (view != null);
        view = (fieldsLayout ? view : BeanUtils.safeType(super.findContentRootView(), ViewGroup.class));
        Assert.notNull(view, "Could not found root layout to add more field layouts!");
        // detect title bar height
        if (!fieldsLayout) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LinearLayout lnLayout = new LinearLayout(this);
            lnLayout.setOrientation(LinearLayout.VERTICAL);
            lnLayout.setLayoutParams(lp);
            view.addView(lnLayout);
            view = lnLayout;
        }
        // show fields
        View firstField = null;
        for(final Iterator<String> it = this.layouts.keySet().iterator(); it.hasNext();) {
            String fieldName = it.next();
            View layout = this.layouts.get(fieldName);
            if (firstField == null && layout.isFocusable() && layout.isEnabled()) {
                firstField = layout;
            }
            view.addView(layout);
        }
        // show actions
        if (this.useSaveAction() || this.useResetAction()) {
            // actions layout
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            lp.topMargin = 15;
            LinearLayout lnLayout = new LinearLayout(this);
            lnLayout.setOrientation(LinearLayout.HORIZONTAL);
            lnLayout.setLayoutParams(lp);
            // save action
            if (this.useSaveAction()) lnLayout.addView(this.getSaveAction());
            // reset action
            if (this.useResetAction()) lnLayout.addView(this.getResetAction());
            view.addView(lnLayout);
        }
        // refresh layout
        view.invalidate();
        // require first field focus
        if (firstField != null) firstField.requestFocus();
    }

    /**
     * Custom field view attributes if necessary.<br>
     * TODO Children class mayber override this methdo for customizing field view attributes
     *
     * @param fieldName field name
     * @param field field view
     */
    protected void customFieldView(String fieldName, View field) {}
    /**
     * Attach data to field on initializing.<br>
     * TODO Children class maybe override this method for attaching data to field view as initialization.
     *
     * @param fieldName field name
     * @param field field view
     * @param value field value
     */
    protected abstract void attachFieldData(String fieldName, View field, Object value);
    /**
     * Detach data from field on committing.<br>
     * TODO Children class maybe override this method for detaching data from field view as committing.
     *
     * @param fieldName field name
     * @param field field view
     */
    protected abstract Object detachFieldData(String fieldName, View field);

    /**
     * Set field caption weight ratio
     *
     * @param ratio field caption weight ratio (0 &lt;= ratio &lt;= 1)
     */
    public final void setLabelRatio(float ratio) {
        Assert.isTrue(0 <= ratio && ratio <= 1, "ratio must be from 0 to 1!");
        boolean applied = false;

        // apply ratio
        for(final Iterator<String> it = this.labels.keySet().iterator(); it.hasNext();) {
            String fieldName = it.next();
            TextView label = BeanUtils.safeType(this.labels.get(fieldName), TextView.class);
            if (label != null) {
                LinearLayout.LayoutParams lp = BeanUtils.safeType(
                        label.getLayoutParams(), LinearLayout.LayoutParams.class);
                if (lp == null) {
                    lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, ratio);
                }
                lp.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                lp.weight = ratio;
                label.setLayoutParams(lp);
                applied = true;
            }
        }

        // refresh screen
        if (applied) this.findContentRootView().invalidate();
    }

    /**
     * Set field weight ratio
     *
     * @param fieldName fiel name to apply
     * @param ratio field weight ratio (0 &lt;= ratio &lt;= 1)
     */
    public final void setFieldRatio(String fieldName, float ratio) {
        Assert.isTrue(0 <= ratio && ratio <= 1, "ratio must be from 0 to 1!");

        // apply ratio
        if (this.fields.containsKey(fieldName)) {
            View fieldView = this.fields.get(fieldName);
            LinearLayout.LayoutParams lp = BeanUtils.safeType(
                    fieldView.getLayoutParams(), LinearLayout.LayoutParams.class);
            if (lp == null) {
                lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, ratio);
            }
            lp.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp.weight = ratio;
            fieldView.setLayoutParams(lp);
            // repaint field
            fieldView.invalidate();

            // check in group
        } else {
            for(final Iterator<String> it = this.groups.keySet().iterator(); it.hasNext();) {
                String group = it.next();
                Map<String, View> fields = this.groups.get(group);
                if (fields.containsKey(fieldName)) {
                    View fieldView = fields.get(fieldName);
                    LinearLayout.LayoutParams lp = BeanUtils.safeType(
                            fieldView.getLayoutParams(), LinearLayout.LayoutParams.class);
                    if (lp == null) {
                        lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, ratio);
                    }
                    lp.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                    lp.weight = ratio;
                    fieldView.setLayoutParams(lp);
                    // repaint field
                    fieldView.invalidate();
                    break;
                }
            }
        }
    }

    /**
     * Set the distance (dp) between fields
     *
     * @param distance the distance (dp) between fields (distance &gt;= 0)
     */
    public final void setFieldDistance(int distance) {
        // apply distance
        distance = Math.max(distance, 0);

        // apply distance
        for(final Iterator<String> it = this.layouts.keySet().iterator(); it.hasNext();) {
            String fieldName = it.next();
            View view = this.layouts.get(fieldName);
            this.setViewDistance(view, distance);
        }

        // refresh screen
        this.findContentRootView().invalidate();
    }
    /**
     * Set the distance (dp) between fields (internal)
     *
     * @param ratio the distance (dp) between fields
     */
    private void setViewDistance(View group, int distance) {
        Assert.notNull(group, "view");
        distance = Math.max(distance, 0);
        ViewGroup vgroup = BeanUtils.safeType(group, ViewGroup.class);
        if (vgroup != null) {
            LinearLayout.LayoutParams lp = BeanUtils.safeType(
                    vgroup.getLayoutParams(), LinearLayout.LayoutParams.class);
            if (lp == null) {
                lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            }
            lp.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            lp.topMargin = distance;
            vgroup.setLayoutParams(lp);
        }
        if (vgroup != null && vgroup.getChildCount() > 0) {
            for(int i = 0; i < vgroup.getChildCount(); i++) {
                this.setViewDistance(vgroup.getChildAt(i), distance);
            }
        }
    }

    /**
     * Get the layout component to add fields.<br>
     * NULL for default root view from content layout.<br>
     * TODO Children classes maybe override this method for customizing the fields layout position.
     * @return the layout component to add fields
     */
    protected ViewGroup findFieldsLayout() {
        return null;
    }

    /* (Non-Javadoc)
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    @Override
    protected final void onPostCreate(Bundle savedInstanceState) {
        // call by super class
        super.onPostCreate(savedInstanceState);

        // initialization GUI
        this.initGUI();

        // show GUI
        this.showGUI();

        // post creation
        this.postCreate(savedInstanceState);
    }
    /**
     * This method has been called after field components have been added
     * and at the latest position in {@link #onPostCreate(Bundle)} method.<br>
     * TODO Children class maybe override this method for perform some actions on postting creation
     * @param savedInstanceState the instance state
     */
    protected void postCreate(Bundle savedInstanceState) {}

    /**
     * Get a boolean value indicating that the items has been viewed vertical/horizontal.<br>
     * TODO Children class maybe override this method for changing view type
     * @return true for horizontal; else false
     */
    protected boolean isHorizontal() {
        return true;
    }

    /**
     * Create element layout component.<br>
     * TODO Children class must override this method to create field component.
     * Or return NULL for default {@link EditText} component
     *
     * @param resId resource identity
     * @param fieldName entity field name
     * @param fieldType entity field declared type
     *
     * @return the element layout component; NULL or creating by default {@link EditText} component
     */
    protected abstract View createFieldComponent(int resId, String fieldName, Class<?> fieldType);

    /**
     * Get the entity class
     * @return the entity class
     */
    public final Class<T> getEntityClass() {
        return this.entityClass;
    }

    /**
     * Get the entity.<br>
     * The entity value is initialized value or committed value base on actions
     *
     * @return the entity data
     */
    public final T getEntity() {
        return this.entity;
    }
    /**
     * Set the entity data.
     * @param entity entity data
     */
    public final void setEntity(T entity) {
        // show values
        if (entity != null) {
            for(final Iterator<String> it = this.fields.keySet().iterator(); it.hasNext();) {
                String fieldName = it.next();
                View fieldView = this.fields.get(fieldName);
                Object value = this.detachFieldData(fieldName, fieldView);
                BeanUtils.setPropertyValue(entity, fieldName, new Object[] { value });
            }
        }
        this.entity = entity;
    }

    /**
     * Create entity data access object (DAO). Default is NULL for creating default.<br>
     * TODO Children classes maybe override this method for customizing entity DAO if necessary
     *
     * @return the entity data access object (DAO)
     */
    protected SqlLiteEntityDao<T> createEntityDao() {
        return null;
    }
    /**
     * Get the entity DAO for inserting/updating/deleting
     *
     * @return the entity DAO
     */
    protected final SqlLiteEntityDao<T> getEntityDao() {
        // create DAO by children classes
        if (this.entityDao == null) {
            this.entityDao = this.createEntityDao();
        }
        // if failed, then creating by default
        if (this.entityDao == null) {
            this.entityDao = new SqlLiteEntityDao<T>(
                    super.getBaseApplication().getDatabase(), this.getEntityClass()) {

                /**
                 * default serial version id
                 */
                private static final long serialVersionUID = 1L;
            };
        }
        synchronized (this.entityDao) {
            return this.entityDao;
        }
    }

    /**
     * Validate the entity data. Default is valid.<br>
     * The entity data maybe required vis {@link #getEntity()} method
     *
     * @return true for valid; else false
     */
    protected boolean validate() {
        return true;
    }

    /**
     * Validate/Commit form data.
     *
     * @throws ApplicationRuntimeException thrown if failed at run-time
     */
    public final void commit() throws ApplicationRuntimeException {
        // cache current entity
        T tmpEntity = this.entity;

        // commit entity
        this.entity = this.getLayoutEntity();

        // validate entity
        boolean validated = this.validate();

        // if validating fail, then roll-back entity
        if (!validated) {
            this.entity = tmpEntity;
        } else {
            // save form data
            boolean successful = this.saveEntity();

            // post saving action
            this.onPostSave(successful);
        }
    }
    /**
     * Get the temporary entity data on layout
     *
     * @return the temporary entity data on layout
     *
     * @throws ApplicationRuntimeException thrown if failed at run-time
     */
    protected final T getLayoutEntity() throws ApplicationRuntimeException {
        T tmpEntity = BeanUtils.safeNewInstance(this.entityClass);
        Assert.notNull(tmpEntity, "Could not found create entity of {" + this.entityClass.getName() + "} class for committing!");
        for(final Iterator<String> it = this.fields.keySet().iterator(); it.hasNext();) {
            String fieldName = it.next();
            View fieldView = this.fields.get(fieldName);
            Object value = this.detachFieldData(fieldName, fieldView);
            LogUtils.i("Set value \"{0}\" for field \"{1}\"",
                    new Object[] { (value == null ? "NULL" : String.valueOf(value)), fieldName });
            if (!BeanUtils.setPropertyValue(tmpEntity, fieldName, value)) {
                LogUtils.w("Could not set value \"{0}\" for field \"{1}\"",
                        new Object[] { (value == null ? "NULL" : String.valueOf(value)), fieldName });
            }
        }
        return tmpEntity;
    }

    /**
     * Get the reset confirmation message. Default is 0.<br>
     * TODO Children class maybe override this method for showing confirmation message while resetting form data.
     *
     * @return the reset confirmation message resource identity
     */
    protected String getResetConfirmation() {
        return null;
    }
    /**
     * Get the positive button title of the reset confirmation dialog. Default is null.<br>
     * TODO Children class maybe override this method for showing the positive button title of the reset confirmation dialog.
     *
     * @return the positive button title of the reset confirmation dialog.
     */
    protected String getResetPositive() {
        return "OK";
    }
    /**
     * Get the negative button title of the reset confirmation dialog. Default is null.<br>
     * TODO Children class maybe override this method for showing the negative button title of the reset confirmation dialog.
     *
     * @return the negative button title of the reset confirmation dialog.
     */
    protected String getResetNegative() {
        return "Cancel";
    }
    /**
     * Roll-back all field modification
     *
     * @throws ApplicationRuntimeException if failed at run-time
     */
    private void resetInternal() throws ApplicationRuntimeException {
        for(final Iterator<String> it = this.fields.keySet().iterator(); it.hasNext();) {
            String fieldName = it.next();
            View fieldView = this.fields.get(fieldName);
            Object value = (this.entity == null ? null
                    : BeanUtils.getFieldValue(this.entity, fieldName));
            this.attachFieldData(fieldName, fieldView, value);
            if (!BeanUtils.setPropertyValue(fieldView, "error", (Object[]) null)) {
                BeanUtils.invokeMethod(fieldView, "setError", true,
                        new Class<?>[] { CharSequence.class }, new Object[] { "" });
            }
        }
    }
    /**
     * Roll-back all field modification
     *
     * @throws ApplicationRuntimeException if failed at run-time
     */
    public final void reset() throws ApplicationRuntimeException {
        String cfmMsg = this.getResetConfirmation();
        if (StringUtils.hasText(cfmMsg)) {
            super.alert(cfmMsg,
                    this.getResetPositive(),
                    new DialogInterface.OnClickListener() {

                        /*
                         * (Non-Javadoc)
                         * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // reset form
                            AbstractEntityActivity.this.resetInternal();
                        }
                    },
                    this.getResetNegative(),
                    null);
        } else {
            // reset form
            AbstractEntityActivity.this.resetInternal();
        }
    }

    /**
     * Specify using default actions: save.<br>
     * TODO Children classes maybe override this method for custom actions
     * @return true for using; else false
     */
    protected boolean useSaveAction() {
        return true;
    }
    /**
     * Get the save action
     * @return the save action
     */
    protected Button getSaveAction() {
        // if using default action
        if (this.saveBtn == null && this.useSaveAction()) {
            this.saveBtn = new Button(this);
            this.saveBtn.setOnClickListener(
                    new OnClickListener() {

                        /*
                         * (Non-Javadoc)
                         * @see android.view.View.OnClickListener#onClick(android.view.View)
                         */
                        @Override
                        public void onClick(View v) {
                            // commit form
                            AbstractEntityActivity.this.commit();
                        }
                    });

            // else if not using default action; should remove it if necessary
        } else if (this.saveBtn != null && !this.useSaveAction()) {
            try {
                ViewGroup parent = BeanUtils.safeType(
                        this.saveBtn.getParent(), ViewGroup.class);
                if (parent != null) {
                    // remove the save action
                    parent.removeView(this.saveBtn);
                    // detect for removing empty layout
                    do {
                        if (parent != null && parent.getChildCount() <= 0) {
                            ViewGroup tmpParent = BeanUtils.safeType(
                                    parent.getParent(), ViewGroup.class);
                            tmpParent.removeView(parent);
                            parent = tmpParent;
                        }
                    } while (parent != null && parent.getChildCount() > 0);
                    // repaint layout
                    parent.invalidate();
                }
            } catch (Exception e) {}
            this.saveBtn = null;
        }
        return this.saveBtn;
    }
    /**
     * Perform actions for inserting/updating entity.<br>
     * TODO Children classes maybe override this method for customizing the saving entity
     *
     * @return true for successful; else false
     */
    protected boolean saveEntity() {
        T entity = this.getEntity();
        long effected = 0;
        boolean updated = false;
        try {
            // check entity existence
            updated = this.getEntityDao().exist(entity);
            // need to update
            if (updated) {
                effected = this.getEntityDao().update(entity);
            } else {
                effected = this.getEntityDao().insert(entity);
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            effected = 0;
        }
        return (effected > 0);
    }
    /**
     * Perform actions after finishing the saving entity.<br>
     * TODO Children classes maybe override this method for performing some actions such as starting another activity
     *
     * @param successful specify entity whether has been inserted/updated successful or failure
     */
    protected void onPostSave(boolean successful) {}

    /**
     * Specify using default actions: save.<br>
     * TODO Children classes maybe override this method for custom actions
     * @return true for using; else false
     */
    protected boolean useResetAction() {
        return true;
    }
    /**
     * Get the reset action
     * @return the reset action
     */
    protected Button getResetAction() {
        // if using default action
        if (this.resetBtn == null && this.useResetAction()) {
            this.resetBtn = new Button(this);
            this.resetBtn.setOnClickListener(
                    new OnClickListener() {

                        /*
                         * (Non-Javadoc)
                         * @see android.view.View.OnClickListener#onClick(android.view.View)
                         */
                        @Override
                        public void onClick(View v) {
                            // reset form
                            AbstractEntityActivity.this.reset();
                        }
                    });

            // else if not using default action; should remove it if necessary
        } else if (this.resetBtn != null && !this.useResetAction()) {
            try {
                ViewGroup parent = BeanUtils.safeType(
                        this.resetBtn.getParent(), ViewGroup.class);
                if (parent != null) {
                    // remove the reset action
                    parent.removeView(this.resetBtn);
                    // detect for removing empty layout
                    do {
                        if (parent != null && parent.getChildCount() <= 0) {
                            ViewGroup tmpParent = BeanUtils.safeType(
                                    parent.getParent(), ViewGroup.class);
                            tmpParent.removeView(parent);
                            parent = tmpParent;
                        }
                    } while (parent != null && parent.getChildCount() > 0);
                    // repaint layout
                    parent.invalidate();
                }
            } catch (Exception e) {}
            this.resetBtn = null;
        }
        return this.resetBtn;
    }

    /**
     * Get the field caption component with entity field name as key
     *
     * @param fieldName the entity field name
     *
     * @return the field caption component or NULL if not found or wrong component class
     */
    public final TextView findFieldLabelByName(String fieldName) {
        TextView view = null;
        if (!CollectionUtils.isEmpty(this.labels)
                && StringUtils.hasText(fieldName)) {
            view = BeanUtils.safeType(this.labels.get(fieldName), TextView.class);
        }
        return view;
    }

    /**
     * Get the field editable component with entity field name as key
     *
     * @param <K> the component type
     * @param fieldName the entity field name
     * @param viewClass view class to check
     *
     * @return the field editable component or NULL if not found or wrong component class
     */
    public final <K extends View> K findFieldViewByName(String fieldName, Class<K> viewClass) {
        K view = null;
        if (!CollectionUtils.isEmpty(this.fields)
                && StringUtils.hasText(fieldName)) {
            view = BeanUtils.safeType(this.fields.get(fieldName), viewClass);
        }
        return view;
    }
    /**
     * Get the whole field layout component with entity field name as key
     *
     * @param <K> the component type
     * @param fieldName the entity field name
     * @param viewClass view class to check
     *
     * @return the whole field layout component or NULL if not found or wrong component class
     */
    public final <K extends View> K findFieldLayoutByName(String fieldName, Class<K> viewClass) {
        K view = null;
        if (!CollectionUtils.isEmpty(this.layouts) && StringUtils.hasText(fieldName)) {
            // if simple field layout
            if (this.layouts.containsKey(fieldName)
                    && !this.groups.containsKey(fieldName)) {
                view = BeanUtils.safeType(this.layouts.get(fieldName), viewClass);

                // find in group
            } else {
                for(final Iterator<String> it = this.groups.keySet().iterator(); it.hasNext();) {
                    String group = it.next();
                    Map<String, View> fields = this.groups.get(group);
                    if (fields.containsKey(fieldName)) {
                        view = BeanUtils.safeType(fields.get(fieldName), viewClass);
                        break;
                    }
                }
            }
        }
        return view;
    }
    /**
     * Get the layout component of group fields with entity field name as key
     *
     * @param <K> the component type
     * @param fieldName the entity field name
     * @param viewClass view class to check
     *
     * @return the layout component of group fields or NULL if not found or wrong component class
     */
    public final <K extends View> K findGroupLayoutByName(String fieldName, Class<K> viewClass) {
        K view = null;
        if (!CollectionUtils.isEmpty(this.layouts) && StringUtils.hasText(fieldName)) {
            if (this.layouts.containsKey(fieldName) && !this.groups.containsKey(fieldName)) {
                view = null;
            } else if (this.layouts.containsKey(fieldName) && this.groups.containsKey(fieldName)) {
                view = BeanUtils.safeType(this.layouts.get(fieldName), viewClass);
            } else {
                for(final Iterator<String> it = this.groups.keySet().iterator(); it.hasNext();) {
                    String group = it.next();
                    Map<String, View> fields = this.groups.get(group);
                    if (!CollectionUtils.isEmpty(fields) && fields.containsKey(fieldName)
                            && this.layouts.containsKey(group)) {
                        view = BeanUtils.safeType(this.layouts.get(group), viewClass);
                        break;
                    }
                }
            }
        }
        return view;
    }

    /**
     * Get the field editable component identity with entity field name as key
     *
     * @param fieldName entity field name
     *
     * @return the field editable component identity or 0 if not found
     */
    public final int findFieldViewIdByName(String fieldName) {
        View view = this.findFieldViewByName(fieldName, View.class);
        return (view == null ? 0 : view.getId());
    }
    /**
     * Get the whole field layout component identity with entity field name as key
     *
     * @param fieldName entity field name
     *
     * @return the whole field layout component identity or 0 if not found
     */
    public final int findFieldLayoutIdByName(String fieldName) {
        View view = this.findFieldLayoutByName(fieldName, View.class);
        return (view == null ? 0 : view.getId());
    }
    /**
     * Get the layout component identity of group fields with entity field name as key
     *
     * @param fieldName entity field name
     *
     * @return the layout component identity of group fields or 0 if not found
     */
    public final int findGroupLayoutIdByName(String fieldName) {
        View view = this.findGroupLayoutByName(fieldName, View.class);
        return (view == null ? 0 : view.getId());
    }
}
