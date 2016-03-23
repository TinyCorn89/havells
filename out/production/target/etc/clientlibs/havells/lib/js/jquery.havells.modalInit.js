/* ----------------------------------
 *	Plugin: modalInit
 *
 *	- Initiates modal functionality
 *  - Opens modal window on click of a.modal
 *
 *
 *  Uses: 	jquery
 *
 * --------------------------------- */

;(function($) {
	$.fn.modalInit = function(argCallBackBefore, argCallBackAfter){

		var fnCallBackBefore = (typeof argCallBackBefore == 'function') ? argCallBackBefore : new Function()
		var fnCallBackAfter = (typeof argCallBackAfter == 'function') ? argCallBackAfter : new Function()


		this.each( function() {
			var $trigger = $(this); // a.modal
			var $goto_modal = $(this.hash); // a.modal
			var $modal = $(this.hash+'-modal.modal');

			$goto_modal.find('a.close, a.closeBtn').unbind('click.closeModal').bind('click.closeModal', function(e){
				e.preventDefault();
				fnCallBackBefore.call($goto_modal);

				$goto_modal.fadeOut('normal', function(){
					$modal.fadeOut('normal', function(){
						fnCallBackAfter.call($goto_modal);
					});
				}); //IE7 sometimes stops the fadeout animation if the parent fades out first.
			 });

			$modal.find('.modal-box').click(function(e){
				e.stopPropagation(); //prevents default close from propagating the modal-box container
			});


			$trigger.click( function(e){
				e.preventDefault();
				// $modal is the parent element of $goto_modal, show the child first, then fade the parent in (this will smooth transistions)
				// Since height isn't available to elements with display:none temporarily shift to visiblity: hidden to get the rendered size.
				// Offset needs to be calculated every click because the viewport may change size (ex: window resize)

				$modal.css({visibility: 'hidden', display: 'block'});
				$goto_modal.css({visibility: 'hidden', display: 'block'});

				var top = (($(window).height() / 2) - ($goto_modal.height() / 2));

				$modal.css({visibility: 'visible', display: 'none'});
				$goto_modal.css({visibility: 'visible', display: 'none'});

				$goto_modal.css({top: top});
				$goto_modal.show();
				$modal.fadeIn();
			});

		}); // .each
	} // end $.fn.modalInit
})(jQuery);