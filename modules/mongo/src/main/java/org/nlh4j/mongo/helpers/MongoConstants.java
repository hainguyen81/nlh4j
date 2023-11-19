/*
 * @(#)MongoConstants.java Aug 22, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.helpers;

/**
 * Mongo database constants definition
 * @author Hai Nguyen
 */
public final class MongoConstants {

	public static final String MONGODB_ORDER_ASC	= "asc";
	public static final String MONGODB_ORDER_DESC	= "desc";

	/**
	 * Mongo database keyword constants definition
	 */
	public static final class MongoKeywords {

		//$
		public static final String MONGODB_DOLLAR_SIGN 	= "$";
		public static final String MONGODB_REGEX 		= "regex_";// search any
		public static final String MONGODB_REGEX_START 	= "regexStart_";	// start character
		public static final String MONGODB_REGEX_START_EXP = "^";
		public static final String MONGODB_REGEX_END 	= "regexEnd_";		// end character
		public static final String MONGODB_REGEX_END_EXP= MONGODB_DOLLAR_SIGN;

		//$eq	Matches values that are equal to a specified value
		public static final String MONGODB_EQ			= "$eq";
		//$gt 	Matches values that are greater than a specified value.
		public static final String MONGODB_GT			= "$gt";
		//$gte 	Matches values that are greater than or equal to a specified value.
		public static final String MONGODB_GTE			= "$gte";
		public static final String MONGODB_GTE_EXP		= "$greater";
		//$in 	Matches any of the values specified in an array.
		public static final String MONGODB_IN			= "$in";
		//$lt 	Matches values that are less than a specified value.
		public static final String MONGODB_LT			= "$lt";
		//$lte 	Matches values that are less than or equal to a specified value.
		public static final String MONGODB_LTE			= "$lte";
		public static final String MONGODB_LTE_EXP		= "$less_lte";
		//$ne 	Matches all values that are not equal to a specified value.
		public static final String MONGODB_NE			= "$ne";
		//$nin 	Matches none of the values specified in an array.
		public static final String MONGODB_NIN			= "$nin";
		//$or 	Matches one of conditions in an array.
		public static final String MONGODB_OR			= "$or";
		//$and 	Matches all conditions in an array.
		public static final String MONGODB_AND			= "$and";
		public static final String MONGODB_NIN_EXP		= "$expnot";
		public static final String MONGODB_INC			= "$inc";
		public static final String MONGODB_ARRAYFILTERS = "arrayFilters";
		//$exists 	Exists condition.
		public static final String MONGODB_EXISTS		= "$exists";
		//$all
		public static final String MONGODB_ALL			= "$all";

		//$unwind
		public static final String MONGODB_UNWIND		= "$unwind";
		public static final String MONGODB_UNWIND_PATH	= "path";
		public static final String MONGODB_UNWIND_PRESERVENULLANDEMPTYARRAYS = "preserveNullAndEmptyArrays";
		//$match
		public static final String MONGODB_MATCH		= "$match";
		//$elemMatch
		public static final String MONGODB_ELEM_MATCH	= "$elemMatch";
		//$expr
		public static final String MONGODB_EXPR			= "$expr";
		//$not
		public static final String MONGODB_NOT			= "$not";
		//$sort
		public static final String MONGODB_SORT			= "$sort";
		//$options
		public static final String MONGODB_OPTIONS		= "$options";
		//$skip
		public static final String MONGODB_SKIP			= "$skip";
		//$limit
		public static final String MONGODB_LIMIT		= "$limit";
		//$expr
		public static final String MONGODB_ADD_FIELDS	= "$addFields";
		//$addToSet
		public static final String MONGODB_ADDTOSET		= "$addToSet";
		//$currentDate
		public static final String MONGODB_CURRENTDATE	= "$currentDate";
		//$currentTimestamp
		public static final String MONGODB_CURRENTTIMESTAMP = "$currentTimestamp";
		//$bit
		public static final String MONGODB_BIT			= "$bit";
		//$bit (and)
		public static final String MONGODB_BIT_AND		= MONGODB_BIT + "(and)";
		//$bit (or)
		public static final String MONGODB_BIT_OR		= MONGODB_BIT + "(or)";
		//$bit (xor)
		public static final String MONGODB_BIT_XOR		= MONGODB_BIT + "(xor)";
		//$pull
		public static final String MONGODB_PULL			= "$pull";
		//$max
		public static final String MONGODB_MAX			= "$max";
		//$min
		public static final String MONGODB_MIN			= "$min";
		//$mul (multiply)
		public static final String MONGODB_MULTIPLY		= "$mul";
		//$pop
		public static final String MONGODB_POP			= "$pop";
		//$rename
		public static final String MONGODB_RENAME		= "$rename";
		//$push
		public static final String MONGODB_PUSH			= "$push";
		//$setOnInsert
		public static final String MONGODB_SETONINSERT	= "$setOnInsert";
		//$unset
		public static final String MONGODB_UNSET		= "$unset";
		//$project
		public static final String MONGODB_PROJET		= "$project";
		//$type
		public static final String MONGODB_TYPE			= "$type";
		public static final String MONGODB_TYPE_ARRAY	= "array";
		//$lookup
		public static final String MONGODB_LOOKUP		= "$lookup";
		public static final String MONGODB_LOOKUP_FROM	= "from";
		public static final String MONGODB_LOOKUP_LOCAL_FIELD = "localField";
		public static final String MONGODB_LOOKUP_FOREIGN_FIELD = "foreignField";
		public static final String MONGODB_LOOKUP_AS	= "as";
		public static final String MONGODB_LOOKUP_PIPELINE = "pipeline";
		public static final String MONGODB_LOOKUP_LET	= "let";

		public static final String MONGODB_SEARCH_PATH_SEPARATE	= "/";

		/**
		 * Initialize a new instance of {@link MongoConstants.MongoKeywords} class
		 */
		private MongoKeywords() {}
	}

	/**
	 * Mongo database field name constants definition
	 */
	public static final class MongoFields {

		public static final String MONGODB_ID 			= "id";
		public static final String MONGODB_ID_VARIABLE	=
				MongoKeywords.MONGODB_DOLLAR_SIGN + MONGODB_ID;
		public static final String MONGODB_OBJECT_ID 	= "_id";
		public static final String MONGODB_OBJECT_ID_VARIABLE =
				MongoKeywords.MONGODB_DOLLAR_SIGN + MONGODB_OBJECT_ID;
		public static final String MONGODB_DELETED_DATE = "deletedDate";
		public static final String MONGODB_DELETED_DATE_VARIABLE =
				MongoKeywords.MONGODB_DOLLAR_SIGN + MONGODB_DELETED_DATE;
		public static final String MONGODB_DELETED_USER = "deletedUser";
		public static final String MONGODB_CREATED_DATE = "createdDate";
		public static final String MONGODB_CREATED_USER = "createdUser";
		public static final String MONGODB_UPDATED_DATE = "updatedDate";
		public static final String MONGODB_UPDATED_USER = "updatedUser";
		public static final String MONGODB_CURRENT_USER	= "currentUser";
		public static final String MONGODB_PATH			= "path";

		/**
		 * Initialize a new instance of {@link MongoConstants.MongoFields} class
		 */
		private MongoFields() {}
	}

	/**
	 * Initialize a new instance of {@link MongoConstants} class
	 */
	private MongoConstants() {}
}
