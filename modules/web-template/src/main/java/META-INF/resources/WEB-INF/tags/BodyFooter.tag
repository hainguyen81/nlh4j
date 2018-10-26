<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- START::notification -->
<!-- NOTE: remove 'animation-class': 'toast-top-center' if you want it on the left top corner -->
<toaster-container toaster-options="{'time-out': 3000, 'close-button':true, 'animation-class': 'toast-top-center', 'position-class': 'toast-top-right'}"></toaster-container>
<!-- END::notification -->

<!--/.footer -->
<footer style="bottom: 0; right: 0; width: auto; z-index: 200; background-color: transparent;">
    <p class="pageTop" style="display: block; z-index: 10;">
    	<a href="javascript:void(0);">
    		<img src="${resourcePath}/image/top-button.png" alt="" width="44" height="44">
    	</a>
    </p>
</footer>
