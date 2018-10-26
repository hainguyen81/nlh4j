<%@ tag pageEncoding="utf-8" description="The &lt;input&gt; HTML element wrapper"%>
<%@ tag import="org.nlh4j.util.DateUtils"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="barcodePrefix" required="false"
description="The regular expression for barcode prefix"%>
<%@ attribute name="barcodeLength" required="true"
description="The number barcode length"%>
<%@ attribute name="scanDuration" required="false"
description="The scanning duration (in milliseconds)"%>
<%@ attribute name="onScan" required="true"
description="Angular block script or function will be called when barcode has been scanned (on-scan)"%>

<barcode-listener
on-scan="${onScan}"
data-prefix="${barcodePrefix}"
data-length="${barcodeLength}"
data-scan-duration="${scanDuration}"></barcode-listener>
