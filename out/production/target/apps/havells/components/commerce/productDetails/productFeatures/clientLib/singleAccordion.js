var accordionCheck = {
    accordionCount: 0,
    notEmptyAccordionCount: 0,
    featureClicker: $(".featureClicker a"),
    incrementAccordionCount: function(isAccordionNotEmpty){
        this.accordionCount++;
        if(isAccordionNotEmpty){
            this.notEmptyAccordionCount++;
        }
    },
    isSingleAccordion: function(){
        if(this.notEmptyAccordionCount <= 2){
            this.featureClicker.trigger('click');
        }
    },
    resetAccordionCounter: function(){
        this.accordionCount = 0;
        this.notEmptyAccordionCount = 0;
    },
    closeAllAccordions: function(){
        this.featureClicker.removeClass("active");
        this.featureClicker.parent().next().slideUp();
        return accordionCheck;
    }
};

$(document).ready(function(){
    var acc = $(".featureClicker a");
    if(acc != undefined){
        accordionCheck.isSingleAccordion();
    }
});

