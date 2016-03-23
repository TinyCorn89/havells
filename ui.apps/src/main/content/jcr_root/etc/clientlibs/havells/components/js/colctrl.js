var ManageFields = {};

ManageFields.columnOptions = function(component){

    var panel = component.findParentByType('panel');
    // get reference of fieldset from the panel

    var fieldSets = panel.findByType('dialogfieldset');
    var fieldSetsLength = fieldSets.length;

    for (var i = 0; i < fieldSetsLength; i++) {
        var fieldSet = fieldSets[i];
        if (fieldSet.getItemId() === component.getValue()) {
            fieldSet.show();
        } else {
            fieldSet.hide();
        }
    }
}
