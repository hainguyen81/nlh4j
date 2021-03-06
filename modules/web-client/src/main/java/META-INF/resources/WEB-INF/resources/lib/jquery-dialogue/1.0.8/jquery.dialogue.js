﻿(function($) {
    var counter = 1;
    var dialogues = {};

    $.dialogue = function(url, options) {
    	var dlgId = (options.id ? options.id : 'uix-dialogue-' + counter++);
    	if (dialogues[dlgId] != null) return;
        var dlgoptions = $.extend({
            id: dlgId,
            autoOpen: false,
            autoResize: true,
            autoPosition: true,
            cache: false,
            width: 'auto',
            height: 'auto',
            // dialog parent container identity
            containment: 'body',
            position: null,
            modal: true,
            dialogClass: 'uix-dialogue',
            closeOnEscape: true,
            title: null,
            data: null,
            open: function(event, ui) {},
            close: function(event, ui) {
            	// ダイアログを削除する。
            	$(this).dialog('destroy').remove();
            	dialogues[dlgId] = null;
            },
            complete: function(response, status, xhr) {
                /* A callback function that is executed when the request completes. */
            },
            error: null,
        }, options);

        // ダイアログ初期
        var content = $('<div id="' + dlgoptions.id + '" class="' + dlgoptions.dialogClass + '-content" style="display:none;"></div>');
        content.load(url, dlgoptions.data, function(response, status, xhr) {
        	switch(xhr.status) {
	        	case 200: {
	        		setTimeout(function() {
	        			if (!dlgoptions.position) {
                    		dlgoptions.position = { my: 'center', at: 'center', of: window };
                    	}
	        			dialogues[dlgoptions.id] = content.dialog(dlgoptions).dialog('open').draggable({
	                    	cancel: dlgoptions.cancel,
	                    	handle: dlgoptions.handle,
	                    }).draggable("option", "containment", dlgoptions.containment);
	                    if (dlgoptions.autoPosition) {
	            	        $(window).resize(function() {
	            	        	if (dialogues[dlgoptions.id]) {
	            	        		dialogues[dlgoptions.id].dialog("option", "position", dlgoptions.position);
	            	        	}
	            	        });
	                    }
	                   	dlgoptions.complete.call($(this), response, status, xhr);
	                }, 100);
	        		break;
	        	}
	        	default: {
	        		if (dlgoptions.error && xhr) {
	        			dlgoptions.error.call($(this), xhr, xhr.status, xhr.statusText);
	        		}
	        		else {
	        			if (jAlert) {
	        				jAlert('エラーが発生しました。', null, null);
	        			}
	        			else if (parent.jAlert) {
	        				parent.jAlert('エラーが発生しました。', null, null);
	        			}
	        			else {
	        				alert('エラーが発生しました。');
	        			}
	        		}
	        		break;
	        	}
        	}
        });

        return content;
    };

    $.dialoguePost = function(url, ajaxOptions, options) {
    	var dlgId = (options.id ? options.id : 'uix-dialogue-' + counter++);
    	if (dialogues[dlgId] != null) {
   			dialogues[dlgId].showDialog(null);
    		return;
    	}
    	// ダイアログのオプション
        var dlgoptions = $.extend({
            id: dlgId,
            autoOpen: options.autoOpen || false,
            autoResize: true,
            autoPosition: true,
            cache: false,
            width: 'auto',
            height: 'auto',
            startup: null,	// 0: normal, 1-minimize
            // dialog parent container identity
            containment: 'body',
            position: null,
            modal: true,
            dialogClass: 'uix-dialogue',
            closeOnEscape: true,
            title: null,
            open: null,
            beforeClose: null, // function(result) {}
            close: function(event, ui) {
            	// ダイアログを削除する。
            	$(this).dialog('destroy').remove();
            	dialogues[dlgId] = null;
            },
            afterClose: null, // function(result) {}
            beforeChangeState: null, // function(state) {}
            changeState: null, // function(state) {}
            beforeHide: null, // function(result) {}
            hide: null, // function(result) {}
            beforeShow: null, // function(result) {}
            show: null, // function(result) {}
            complete: null,
            error: null,
        }, options);
        // AJAXのオプション
        var dlgAjaxOptions = $.extend({
        	async: false,
			cache: false,
			url: url,
			type: 'POST',
			data: null,
		    statusCode: {
		    	200: function(data) {
		    		if (data != null && data != undefined && data.length > 0) {
			            // ダイアログ初期
		    			var ok = false;
			    		var iframe = $('<iframe width="100%" scrolling="no" frameborder="0" height="100%" name="IFRAME-' + dlgId + '" id="IFRAME-' + dlgId + '" class="iframe-' + dlgoptions.dialogClass + '-content" src="" style="position: absolute"></iframe>');
			    		var content = $('<div id="' + dlgId + '" class="' + dlgoptions.dialogClass + '-content" style="display:none;"></div>');
			    		iframe.bind('load', function() {
			    			var iframeElem = $(this).get(0);
			    			var win = (iframeElem ? (iframeElem.contentWindow ? iframeElem.contentWindow : (iframeElem.contentDocument ? iframeElem.contentDocument : null)) : null);
		                    var doc = (win && win.document ? win.document : null);
		                    if (doc != null) {
		                    	// ダイアログを削除する。
		                    	iframeElem.closeDialog = function(returnedValue) {
			                    	if (dialogues[dlgId] != null) {
			                    		if (dlgoptions.beforeClose) {
			                    			dlgoptions.beforeClose(returnedValue);
			                    		}
			                    		dialogues[dlgId].dialog('close');
			                    		if (dlgoptions.afterClose) {
			                    			dlgoptions.afterClose(returnedValue);
			                    		}
			                    	}
			                    	dialogues[dlgId] = null;
			                    };
			                    iframeElem.changeDialogState = function(state) {
			                    	state = (state == 0 || state == 1 ? state : 0);
                    				if (dlgoptions.beforeChangeState) {
		                    			dlgoptions.beforeChangeState(state);
		                    		}
                    				switch(state)
                    				{
                    					case 0:
                    					{
                    						dialogues[dlgId].data('state', 1);
    		                    			dialogues[dlgId].data('height', dialogues[dlgId].height());
    		                    			dialogues[dlgId].dialog("option", "height", 0);
    		                    			dialogues[dlgId].data('width', dialogues[dlgId].width());
    		                    			dialogues[dlgId].dialog("option", "width", dialogues[dlgId].width() / 2);
    		                    			state = 1;
    		                    			break;
                    					}
                    					case 1: {
                    						var dlgheight = ($(doc).height() + $('.ui-dialog-titlebar', $('div[aria-describedby="' + dlgId + '"]')).height() + 10);
    	                    				dialogues[dlgId].data('state', 0);
    	                    				dialogues[dlgId].dialog('option', 'height', dlgheight);
    	                    				dialogues[dlgId].data('height', 0);
    	                    				dialogues[dlgId].dialog('option', 'width', dialogues[dlgId].data('width'));
    	                    				dialogues[dlgId].data('width', 0);
    	                    				state = 0;
    	                    				break;
                    					}
                    					default:
                    					{
                    						return;
                    					}
                    				}

		                    		if (dlgoptions.changeState) {
		                    			dlgoptions.changeState(state);
		                    			$(this).data('state', state);
		                    		}
			                    };
			                    iframeElem.getDialogState = function() {
			                    	var state = $(this).data('state');
			                    	state = (!state ? 0 : state);
			                    	return state;
			                    };
			                    iframeElem.hideDialog = function(returnedValue) {
			                    	var dialog = $('div[aria-describedby="' + dlgId + '"]');
                    				if (dialog) {
                    					if (dlgoptions.beforeHide) {
			                    			dlgoptions.beforeHide(returnedValue);
			                    		}

                    					dialog.css('display', 'none');
                    					dialog.css('visibility', 'hidden');
                    					dialog.data('shown', false);

			                    		if (dlgoptions.hide) {
			                    			dlgoptions.hide(returnedValue);
			                    		}
                    				}
			                    };
			                    iframeElem.showDialog = function(returnedValue) {
			                    	var dialog = $('div[aria-describedby="' + dlgId + '"]');
                    				if (dialog) {
                    					if (dlgoptions.beforeShow) {
			                    			dlgoptions.beforeShow(returnedValue);
			                    		}

                    					// show dialog
                    					dialog.css('display', 'block');
                    					dialog.css('visibility', 'visible');
                    					dialog.data('shown', true);

                    					// restore dialog height to original
                    					var dlgheight = ($(doc).height() + $('.ui-dialog-titlebar', $('div[aria-describedby="' + dlgId + '"]')).height() + 10);
	                    				dialogues[dlgId].data('state', 0);
	                    				dialogues[dlgId].dialog('option', 'height', dlgheight);
	                    				dialogues[dlgId].data('height', 0);
	                    				if (dlgoptions.autoPosition) {
			                    			if (!dlgoptions.position) {
			    	                    		dlgoptions.position = { my: 'center', at: 'center', of: window };
			    	                    	}
			                    			dialogues[dlgId].dialog("option", "position", dlgoptions.position);
			                    		}

			                    		if (dlgoptions.show) {
			                    			dlgoptions.show(returnedValue);
			                    		}
                    				}
			                    };

			                    if (dlgoptions.closeOnEscape) {
			                    	iframeElem.keydown = function(e) {
			                    		if (e.keyCode == 27 && this.closeDialog) {
			                    			this.closeDialog(null);
			                    		}
			                    	};
		                    	}

			                    // document listeners
			                    $(doc).ready(function() {
			                    	if (dlgoptions.closeOnEscape) {
				                    	$(doc).keydown(function(e) {
				                    		if (e.keyCode == 27 && iframeElem.closeDialog) {
				                    			iframeElem.closeDialog(null);
				                    		}
				                    	});

				                    	if ($('body', $(doc)).length > 0) {
				                    		$('body', $(doc)).bind('keydown', function(e) {
				                    			if (e.keyCode == 27 && iframeElem.closeDialog) {
					                    			iframeElem.closeDialog(null);
					                    		}
				                    		});
				                    	}
				                    }

			                    	// functions router
			                    	$(doc)[0].closeDialog = function(returnedValue) {
			                    		iframeElem.closeDialog(returnedValue);
			                    	};
			                    	$(doc)[0].hideDialog = function(returnedValue) {
			                    		iframeElem.hideDialog(returnedValue);
				                    };
				                    $(doc)[0].showDialog = function(returnedValue) {
				                    	iframeElem.showDialog(returnedValue);
				                    };
				                    $(doc)[0].changeDialogState = function(state) {
				                    	iframeElem.changeDialogState(state);
				                    };
				                    $(doc)[0].getDialogState = function() {
				                    	return iframeElem.getDialogState();
				                    };

				                    // dialog title bar
			                    	var title = $(dlgoptions.handle, $(doc));
			                    	if (title.length > 0) {
			                    		title.remove();
			                    		$('.ui-dialog-titlebar', $('div[aria-describedby="' + dlgId + '"]')).html('');
			                    		$('.ui-dialog-titlebar', $('div[aria-describedby="' + dlgId + '"]')).append(title);
			                    		if ($('.ui-dialog-titlebar-close', title).length > 0) {
			                    			$('.ui-dialog-titlebar-close', title).click(function() {
				                    			iframeElem.closeDialog(null);
				                    		});
			                    		}
			                    		if ($('.ui-dialog-titlebar-minimize', title).length > 0) {
			                    			$('.ui-dialog-titlebar-minimize', title).click(function() {
			                    				var state = ($(this).hasClass('fa-plus') ? 1 : 0);
			                    				switch(state)
			                    				{
			                    					case 0:
			                    					{
			                    						$(this).removeClass('fa-minus');
						                    			$(this).addClass('fa-plus');
						                    			break;
			                    					}
			                    					case 1:
		                    						{
			                    						$(this).removeClass('fa-plus');
						                    			$(this).addClass('fa-minus');
			                    						break;
		                    						}

			                    				}
			                    				iframeElem.changeDialogState(state);
				                    		});
			                    		}
			                    		if ($('.ui-dialog-titlebar-showhide', title).length > 0) {
			                    			$('.ui-dialog-titlebar-showhide', title).click(function() {
			                    				var dialog = $('div[aria-describedby="' + dlgId + '"]');
			                    				if (dialog
			                    						&& (!dialog.data('shown')
			                    								|| (dialog.data('shown') == 'true' || dialog.data('shown') == '1'))) {
			                    					iframeElem.hideDialog(null);
			                    				}
			                    				else if (dialog && dialog.data('shown')
			                    						&& (dialog.data('shown') == 'false' || dialog.data('shown') == '0')) {
			                    					iframeElem.showDialog(null);
			                    				}
				                    		});
			                    		}
			                    		$('.ui-dialog-titlebar', $('div[aria-describedby="' + dlgId + '"]')).css('display', 'block');
			                    		$('.ui-dialog-titlebar', $('div[aria-describedby="' + dlgId + '"]')).css('visibility', 'visible');
			                    	}

			                    	// resize on startup
			                    	if (dlgoptions.autoResize && dialogues[dlgId] != null) {
				                    	if (!dlgoptions.startup) dlgoptions.startup = 0;
				                    	switch(dlgoptions.startup) {
				                    		case 1: {
				                    			iframeElem.changeDialogState(0);
				                    			break;
				                    		}
				                    	}
			                    	}
			                    });

			                    // resize on startup
			                    if (dlgoptions.autoResize && dialogues[dlgId] != null) {
			                    	if (!dlgoptions.startup) dlgoptions.startup = 0;
			                    	switch(dlgoptions.startup) {
			                    		case 0: {
			                    			var dlgheight = ($(doc).height() + $('.ui-dialog-titlebar', $('div[aria-describedby="' + dlgId + '"]')).height() + 10);
					                    	dialogues[dlgId].dialog('option', 'height', dlgheight);
					                    	break;
			                    		}
			                    	}
		                    	}
			                    if (dlgoptions.autoPosition && dialogues[dlgId] != null) {
	                    			if (!dlgoptions.position) {
	    	                    		dlgoptions.position = { my: 'center', at: 'center', of: window };
	    	                    	}
	                    			dialogues[dlgId].dialog("option", "position", dlgoptions.position);
	                    		}
		                    }
			    		});

			    		// start dialog
			    		if (!dlgoptions.position) {
                    		dlgoptions.position = { my: 'center', at: 'center', of: window };
                    	}
			    		if (dlgoptions.autoOpen) {
				    		dialogues[dlgId] = content.append(iframe).dialog(dlgoptions).dialog('open').draggable({
		                    	cancel: dlgoptions.cancel,
		                    	handle: dlgoptions.handle,
		                    }).draggable("option", "containment", dlgoptions.containment);
			    		}
			    		else {
			    			dialogues[dlgId] = content.append(iframe).dialog(dlgoptions).draggable({
		                    	cancel: dlgoptions.cancel,
		                    	handle: dlgoptions.handle,
		                    }).draggable("option", "containment", dlgoptions.containment);
			    		}
			    		dialogues[dlgId].closeDialog = function(returnedValue) {
                    		iframeElem.closeDialog(returnedValue);
                    	};
                    	dialogues[dlgId].hideDialog = function(returnedValue) {
                    		iframeElem.hideDialog(returnedValue);
	                    };
	                    dialogues[dlgId].showDialog = function(returnedValue) {
	                    	iframeElem.showDialog(returnedValue);
	                    };
	                    dialogues[dlgId].changeDialogState = function(state) {
	                    	iframeElem.changeDialogState(state);
	                    };
	                    dialogues[dlgId].getDialogState = function() {
	                    	return iframeElem.getDialogState();
	                    };
	                    if (dlgoptions.autoPosition) {
	                    	$(window).resize(function() {
	            	        	if (dialogues[dlgId]) {
	            	        		dialogues[dlgId].dialog("option", "position", dlgoptions.position);
	            	        	}
	            	        });
	                    }

	                    // ダイアログの内容初期
	                    var iframeElem = iframe.get(0);
	                    var win = (iframeElem ? (iframeElem.contentWindow ? iframeElem.contentWindow : (iframeElem.contentDocument ? iframeElem.contentDocument : null)) : null);
	                    var doc = (win && win.document ? win.document : null);
	                    if (doc != null) {
		                    doc.open();
		                    doc.write(data);
		                    doc.close();
		                    ok = true;
	                    }

	                    if (ok) {
		                    return;
	                    }
	                    else {
	                    	content.remove();
	                    }
			    	}

		    		// エラー
		    		if (jAlert) {
        				jAlert('エラーが発生しました。', null, null);
        			}
        			else if (parent.jAlert) {
        				parent.jAlert('エラーが発生しました。', null, null);
        			}
        			else {
        				alert('エラーが発生しました。');
        			}
    				// ダイアログを削除する。
                	if (dialogues[dlgId] != null) {
                		dialogues[dlgId].dialog('close');
                	}
                	dialogues[dlgId] = null;
		    	}
		    },
		    // エラー
			error: function(xhr, status, errorThrown) {
				// エラー
				if (dlgoptions.error && xhr) {
        			dlgoptions.error.call($(this), xhr, xhr.status, xhr.statusText);
        		}
        		else {
        			if (jAlert) {
        				jAlert('エラーが発生しました。', null, null);
        			}
        			else if (parent.jAlert) {
        				parent.jAlert('エラーが発生しました。', null, null);
        			}
        			else {
        				alert('エラーが発生しました。');
        			}
        		}
				// ダイアログを削除する。
				if (dialogues[dlgId] != null) {
            		dialogues[dlgId].dialog('close');
            	}
            	dialogues[dlgId] = null;
			},
        }, ajaxOptions);

        // AJAXを実行します。
        $.ajax(dlgAjaxOptions);
    };

    // Shortuct functions
    jDialogue = function(url, options) {
    	$.dialogue(url, options);
    };
    jDialoguePost = function(url, ajaxOptions, options) {
    	$.dialoguePost(url, ajaxOptions, options);
    };
}(jQuery));