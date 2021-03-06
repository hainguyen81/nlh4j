// jQuery Alert Dialogs Plugin
//
// Version 1.1
//
// Cory S.N. LaViska
// A Beautiful Site (http://abeautifulsite.net/)
// 14 May 2009
//
// Visit http://abeautifulsite.net/notebook/87 for more information
//
// Usage:
//		jAlert( message, [title, callback] )
//		jConfirm( message, [title, callback] )
//		jPrompt( message, [value, title, callback] )
//
// History:
//
//		1.00 - Released (29 December 2008)
//
//		1.01 - Fixed bug where unbinding would destroy all resize events
//
// License:
//
// This plugin is dual-licensed under the GNU General Public License and the MIT License and
// is copyright 2008 A Beautiful Site, LLC.
//
(function($) {

	$.alerts = {

		// These properties can be read/written by accessing $.alerts.propertyName from your scripts at any time

		verticalOffset: -75,                // vertical offset of the dialog from center screen, in pixels
		horizontalOffset: 0,                // horizontal offset of the dialog from center screen, in pixels/
		repositionOnResize: true,           // re-centers the dialog on window resize
		overlayOpacity: .20,                // transparency level of overlay
		overlayColor: '#000000',               // base color of overlay
		draggable: true,                    // make the dialogs draggable (requires UI Draggables plugin)
		closeButton: '&nbsp;閉じる&nbsp;',  // text for the close button
		okButton: '&nbsp;はい&nbsp;',       // text for the OK button
		cancelButton: '&nbsp;いいえ&nbsp;', // text for the Cancel button
		dialogClass: 'buttom_style',        // if specified, this class will be applied to all dialogs
		_showedTitle: null,
		_showedContainer: null,

		// Public methods

		setup: function(settings) {
			// vertical offset of the dialog from center screen, in pixels
			$.alerts.verticalOffset = (isNaN(settings.verticalOffset) ? -75 : settings.verticalOffset);
			// horizontal offset of the dialog from center screen, in pixels/
			$.alerts.horizontalOffset = (isNaN(settings.horizontalOffset) ? 0 : settings.horizontalOffset);
			// re-centers the dialog on window resize
			$.alerts.repositionOnResize = (settings.repositionOnResize || true);
			// transparency level of overlay
			$.alerts.overlayOpacity = (isNaN(settings.overlayOpacity) ? .20
					: (Math.abs(settings.overlayOpacity) > 1
							? Math.abs(settings.overlayOpacity) / 100 : Math.abs(settings.overlayOpacity)));;
			// base color of overlay
			$.alerts.overlayColor = (settings.overlayColor == null || settings.overlayColor.length <= 0
					? '#000000' : settings.overlayColor);
			// make the dialogs draggable (requires UI Draggables plugin)
			$.alerts.draggable = (settings.draggable || true);
			// text for the close button
			$.alerts.closeButton = (settings.closeButton == null || settings.closeButton.length <= 0
					? '&nbsp;閉じる&nbsp;' : settings.closeButton);
			// text for the OK button
			$.alerts.okButton = (settings.okButton == null || okButton.length <= 0
					? '&nbsp;はい&nbsp;' : settings.okButton);
			// text for the Cancel button
			$.alerts.cancelButton = (settings.cancelButton == null || settings.cancelButton.length <= 0
					? '&nbsp;いいえ&nbsp;' : settings.cancelButton);
			// if specified, this class will be applied to all dialogs
			$.alerts.dialogClass = (settings.dialogClass == null || settings.dialogClass.length <= 0
					? 'buttom_style' : settings.dialogClass);
		},

		alert: function(message, title, callback) {
			if( title == null ) title = 'noTitle';
			$.alerts._show(title, message, null, 'alert', function(result) {
				if( callback ) callback(result);
			});
		},

		confirm: function(message, title, callback) {
			if( title == null ) title = 'noTitle';
			$.alerts._show(title, message, null, 'confirm', function(result) {
				if( callback ) callback(result);
			});
		},

		prompt: function(message, value, title, callback) {
			if( title == null ) title = 'noTitle';
			$.alerts._show(title, message, value, 'prompt', function(result) {
				if( callback ) callback(result);
			});
		},

		popup: function(title, url, options) {
			$.alerts._showPopup(title, url, options);
		},

		// Private methods

		_show: function(title, msg, value, type, callback) {

			$.alerts._hide(title);
			$.alerts._overlay('show');
			if (title == 'noTitle') {
				$.alerts._showedTitle = title;
				$.alerts._showedContainer = 'popup_container';

	            $("BODY").append(
	                    '<div id="popup_container">' +
	                      '<div id="popup_content">' +
	                        '<div id="popup_message"  style="margin-left: 60px"></div>' +
	                      '</div>' +
	                    '</div>');

	            if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);

	            // IE6 Fix($.browserが1.3からサポート外になっているので動作しない)
	            // var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed';
	            var pos = (navigator.userAgent.match(/msie [6.]/i)) ? 'absolute' : 'fixed';

	            $("#popup_container").css({
	                position: pos,
	                zIndex: 99999,
	                padding: 0,
	                margin: 0
	            });

	            $("#popup_title").text(title);
	            $("#popup_content").addClass(type);
	            $("#popup_message").text(msg);
	            $("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );

	            $("#popup_container").css({
	                //minWidth: $("#popup_container").outerWidth(),
	                //maxWidth: $("#popup_container").outerWidth()
	            });

	            $.alerts._reposition(title, 'popup_container');
	            $.alerts._maintainPosition(true);

	            switch( type ) {
	                case 'alert':
	                    $("#popup_message").after('<div id="popup_panel"><input type="button" value="' + $.alerts.closeButton + '" id="popup_ok" /></div>');
	                    $("#popup_ok").click( function() {
	                        $.alerts._hide(title);
	                        callback(true);
	                    });
	                    $("#popup_ok").focus().keypress( function(e) {
	                        if( e.keyCode == 13 || e.keyCode == 27 ) $("#popup_ok").trigger('click');
	                    });
	                break;
	                case 'confirm':
	                    $("#popup_message").after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
	                    $("#popup_ok").click( function() {
	                        $.alerts._hide(title);
	                        if( callback ) callback(true);
	                    });
	                    $("#popup_cancel").click( function() {
	                        $.alerts._hide(title);
	                        if( callback ) callback(false);
	                    });
	                    $("#popup_ok").focus();
	                    $("#popup_ok, #popup_cancel").keypress( function(e) {
	                        if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
	                        if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
	                    });
	                break;
	                case 'prompt':
	                    $("#popup_message").append('<br /><input type="text" size="30" id="popup_prompt" />').after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
	                    $("#popup_prompt").width( $("#popup_message").width() );
	                    $("#popup_ok").click( function() {
	                        var val = $("#popup_prompt").val();
	                        $.alerts._hide(title);
	                        if( callback ) callback( val );
	                    });
	                    $("#popup_cancel").click( function() {
	                        $.alerts._hide(title);
	                        if( callback ) callback( null );
	                    });
	                    $("#popup_prompt, #popup_ok, #popup_cancel").keypress( function(e) {
	                        if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
	                        if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
	                    });
	                    if( value ) $("#popup_prompt").val(value);
	                    $("#popup_prompt").focus().select();
	                break;
	            }

	            // Make draggable
	            if( $.alerts.draggable ) {
	                try {
	                    $("#popup_container").draggable({
	                        handle: $("#popup_content"),
	                        containment: "document"
	                    });
	                    $("#popup_title").css({ cursor: 'move' });
	                } catch(e) { /* requires jQuery UI draggables */ }
	            }

			} else {

				$.alerts._showedTitle = title;
				$.alerts._showedContainer = 'popup_container_OnTitle';

		         $("BODY").append(
		                 '<div id="popup_container_OnTitle">' +
		                   '<h1 id="popup_title"></h1>' +
		                   '<div id="popup_content_noImage">' +
		                     '<div id="popup_message" style="margin-left: 15px"></div>' +
		                   '</div>' +
		                 '</div>');

		         if( $.alerts.dialogClass ) $("#popup_container_OnTitle").addClass($.alerts.dialogClass);

		            // IE6 Fix($.browserが1.3からサポート外になっているので動作しない)
		            // var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed';
		            var pos = (navigator.userAgent.match(/msie [6.]/i)) ? 'absolute' : 'fixed';

		            $("#popup_container_OnTitle").css({
		                position: pos,
		                zIndex: 99999,
		                padding: 0,
		                margin: 0
		            });

		            $("#popup_title").text(title);
		            $("#popup_content_noImage").addClass(type);
		            $("#popup_message").text(msg);
		            $("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );

		            $("#popup_container_OnTitle").css({
		                //minWidth: $("#popup_container_OnTitle").outerWidth(),
		                //maxWidth: $("#popup_container_OnTitle").outerWidth()
		            });

		            $.alerts._reposition(title, 'popup_container_OnTitle');
	                $.alerts._maintainPosition(true);

		            switch( type ) {
		                case 'alert':
		                    $("#popup_message").after('<div id="popup_panel"><input type="button" value="' + $.alerts.closeButton + '" id="popup_ok" /></div>');
		                    $("#popup_ok").click( function() {
		                        $.alerts._hide(title);
		                        callback(true);
		                    });
		                    $("#popup_ok").focus().keypress( function(e) {
		                        if( e.keyCode == 13 || e.keyCode == 27 ) $("#popup_ok").trigger('click');
		                    });
		                break;
		                case 'confirm':
		                    $("#popup_message").after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
		                    $("#popup_ok").click( function() {
		                        $.alerts._hide(title);
		                        if( callback ) callback(true);
		                    });
		                    $("#popup_cancel").click( function() {
		                        $.alerts._hide(title);
		                        if( callback ) callback(false);
		                    });
		                    $("#popup_ok").focus();
		                    $("#popup_ok, #popup_cancel").keypress( function(e) {
		                        if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
		                        if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
		                    });
		                break;
		                case 'prompt':
		                    $("#popup_message").append('<br /><input type="text" size="30" id="popup_prompt" />').after('<div id="popup_panel"><input type="button" value="' + $.alerts.okButton + '" id="popup_ok" /> <input type="button" value="' + $.alerts.cancelButton + '" id="popup_cancel" /></div>');
		                    $("#popup_prompt").width( $("#popup_message").width() );
		                    $("#popup_ok").click( function() {
		                        var val = $("#popup_prompt").val();
		                        $.alerts._hide(title);
		                        if( callback ) callback( val );
		                    });
		                    $("#popup_cancel").click( function() {
		                        $.alerts._hide(title);
		                        if( callback ) callback( null );
		                    });
		                    $("#popup_prompt, #popup_ok, #popup_cancel").keypress( function(e) {
		                        if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
		                        if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
		                    });
		                    if( value ) $("#popup_prompt").val(value);
		                    $("#popup_prompt").focus().select();
		                break;
		            }

		            // Make draggable
		            if( $.alerts.draggable ) {
		                try {
		                    $("#popup_container_OnTitle").draggable({
		                        handle: $("#popup_title"),
		                        containment: "document"
		                    });
		                    $("#popup_title").css({ cursor: 'move' });
		                } catch(e) { /* requires jQuery UI draggables */ }
		            }
			}
		},

		_showPopup: function(title, url, options) {

			$.alerts._showedTitle = title;
			$.alerts._showedContainer = 'popup_container';

			$.alerts._hide(title);
			$.alerts._overlay('show');

			$("BODY").append(
                    '<div id="popup_container">' +
                       '<h1 id="popup_title"></h1>' +
	                   '<div id="popup_content_2">' +
                        '<div id="popup_message" style="margin-left: 0; padding: 0;">' +
                          '<iframe id="popup_page"></iframe>' +
                        '</div>' +
                      '</div>' +
                    '</div>');

            if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);

            // IE6 Fix($.browserが1.3からサポート外になっているので動作しない)
            // var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed';
            var pos = (navigator.userAgent.match(/msie [6.]/i)) ? 'absolute' : 'fixed';

            var winOpts = {
            		position: pos,
                    zIndex: 99999,
                    padding: 0,
                    margin: 0,
        	};
            if (options.winCss) { $.extend(winOpts, options.winCss); }
        	$("#popup_container").css(winOpts);

            if (title == null || title == undefined || title.length <= 0) {
            	$("#popup_title").remove();
            }
            else {
            	$("#popup_title").text(title);
            }

            var heightIframe = null;
            if(winOpts.height == null || winOpts.height == undefined){
            	heightIframe = "100%";
            }else{
            	heightIframe = winOpts.height;
            }
            var ifOpts = {
        			src: url,
        			scrolling: "1",
        			frameborder: "0",
        			width: "100%",
        			height: heightIframe,
        	};
        	if (options.frameAttributes) { $.extend(ifOpts, options.frameAttributes); }
        	$("#popup_page").attr(ifOpts);
            $.alerts._reposition(title, 'popup_container');
            $.alerts._maintainPosition(true);

            // Make draggable
            if( $.alerts.draggable ) {
                try {
                    $("#popup_container").draggable({
                        handle: $("#popup_content"),
                        containment: "document"
                    });
                    $("#popup_title").css({ cursor: 'move' });
                } catch(e) { /* requires jQuery UI draggables */ }
            }
		},

		_hide: function(title) {
		    if (title == 'noTitle') {
		        $("#popup_container").remove();
		    } else {
		        $("#popup_container_OnTitle").remove();
		    }
			$.alerts._overlay('hide', title);
			$.alerts._maintainPosition(false);
			$.alerts._showedTitle = null;
			$.alerts._showedContainer = null;
		},

		_overlay: function(status, title) {
			switch( status ) {
				case 'show':
					$.alerts._overlay('hide');
					$("BODY").append('<div id="popup_overlay"></div>');
					$("#popup_overlay").css({
						position: 'absolute',
						zIndex: 99998,
						top: '0px',
						left: '0px',
						width: '100%',
						height: $(document).height(),
						//height: $("#main_child", parent.document).height(),
						background: $.alerts.overlayColor,
						opacity: $.alerts.overlayOpacity
					});
				break;
				case 'hide':
					$("#popup_overlay").remove();
				break;
			}
		},

		_reposition: function(title, containerId) {
		    var top = null;
		    var left = null;
		    top = (($(window).height() / 2) - ($("#" + containerId).outerHeight() / 2)) + $.alerts.verticalOffset;
            left = (($(window).width() / 2) - ($("#" + containerId).outerWidth() / 2)) + $.alerts.horizontalOffset;
            if( top < 0 ) top = 0;
            if( left < 0 ) left = 0;

            // IE6 Fix($.browserが1.3からサポート外になっているので動作しない)
            // if( $.browser.msie && parseInt($.browser.version) <= 6 ) top = top + $(window).scrollTop();
            if(navigator.userAgent.match(/msie [6.]/i)) top = top + $(window).scrollTop();

            $("#" + containerId).css({
                top: top + 'px',
                left: left + 'px'
            });
            $("#popup_overlay").height( $(document).height() );
		},

		_repositionListener: function() {
			$.alerts._reposition($.alerts._showedTitle, $.alerts._showedContainer);
		},

		_maintainPosition: function(status) {
			if( $.alerts.repositionOnResize ) {
				switch(status) {
					case true:
						$(window).bind('resize', $.alerts._repositionListener);
					break;
					case false:
						$(window).unbind('resize', $.alerts._repositionListener);
					break;
				}
			}
		}

	};

	// Shortuct functions
	jAlertSetup = function(settings) {
		$.alerts.setup(settings);
	};

	jAlert = function(message, title, callback) {
		$.alerts.alert(message, title, callback);
	};

	jConfirm = function(message, title, callback) {
		$.alerts.confirm(message, title, callback);
	};

	jPrompt = function(message, value, title, callback) {
		$.alerts.prompt(message, value, title, callback);
	};

	jPopup = function(url, title, options) {
		$.alerts.popup(title, url, options);
	};
})(jQuery);