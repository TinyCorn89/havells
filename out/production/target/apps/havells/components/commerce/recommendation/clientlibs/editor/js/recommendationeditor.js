/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2014 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 *************************************************************************/
CQ.Recommendations = CQ.Recommendations || {};

CQ.Recommendations.Authoring = CQ.Recommendations.Authoring || function() {
    "use strict";
    var self = {};

    /**
     * Toggle source manual specify field visibility based on the selected alg
     * source
     */
    self.showHideManualSource = function(field, clearManualValue) {
        var panel = field.findParentByType("panel");

        var algorithmSource = _getFieldByName(panel, "./recSource");
        var algorithm = _getFieldByName(panel, "./algorithm");
        var hiddenManualSourceField = _getFieldByName(panel, "./manualSource");

        var isManual = algorithmSource.getValue() == "manual";

        var selectedAlg = algorithm.getValue();

        var manualSourceField = _getFieldByName(panel, "manualSourceVisible");
        if (manualSourceField) {
            panel.remove(manualSourceField);
        }

        if (isManual) {
            if (selectedAlg == "tags") {
                manualSourceField = new CQ.tagging.TagInputField({
                    name: "manualSourceVisible",
                    emptyText: "",
                    fieldLabel: "Manually specify",
                    allowBlank: "{Boolean}true",
                    fieldDescription: "Manually specify tag values",
                    listeners: {
                        addTag: function(tagInputField, tag){
                            CQ.Recommendations.Authoring.updateManualValue(tagInputField);
                        },
                        removeTag: function(tagInputField, tag) {
                            CQ.Recommendations.Authoring.updateManualValue(tagInputField);
                        }
                    }
                });
            } else {
                manualSourceField = new CQ.form.MultiField({
                    name: "manualSourceVisible",
                    emptyText: "",
                    fieldLabel: "Manually specify",
                    allowBlank: "{Boolean}true",
                    fieldDescription: "Manually specify category values",
                    fieldConfig: {
                        allowBlank: "{Boolean}false",
                        xtype: "textfield",
                        listeners: {
                            change: function(field, newValue, oldValue) {
                                CQ.Recommendations.Authoring.updateManualValue(field);
                            }
                        }
                    },
                    listeners: {
                        removeditem: function(multifield) {
                            CQ.Recommendations.Authoring.updateManualValue(multifield);
                        }
                    }
                });
            }
            if (!clearManualValue) {
                manualSourceField.setValue(hiddenManualSourceField.getValue().split(","));
            } else {
                manualSourceField.setValue([]);
            }

            var hiddenSourceFieldIndex = _getFieldIndex(panel, hiddenManualSourceField);

            if (hiddenSourceFieldIndex >= 0) {
                panel.insert(hiddenSourceFieldIndex, manualSourceField);
            } else {
                panel.add(manualSourceField);
            }

            panel.doLayout();
        }
    };

    self.updateManualValue = function(valueSource) {
        var panel = valueSource.findParentByType("dialog");
        var manualField = _getFieldByName(panel, "./manualSource");

        if (valueSource.findParentByType("multifield")) {
            valueSource = valueSource.findParentByType("multifield");
        }

        manualField.setValue("");
        manualField.setValue(valueSource.getValue());
    }

    var _getFieldIndex = function(panel, field) {
        if (panel
                && panel.items
                && panel.items.items) {
            for (var itemIdx = 0 ; itemIdx < panel.items.items.length ; itemIdx ++) {
                if (field == panel.items.items[itemIdx]) {
                    return itemIdx;
                }
            }
        }

        return -1;
    }

    var _getFieldsByName = function(panel, fieldName) {
        var fields = undefined;

        if (panel) {
            var fieldArray = panel.findBy(function(fieldObj){
                if (fieldObj
                        && fieldObj.getName) {
                    return fieldObj.getName() == fieldName;
                }
            }, this);

            if (fieldArray
                    && fieldArray.length > 0) {
                fields = fieldArray;
            }
        }

        return fields;
    };

    var _getFieldByName = function(panel, fieldName) {
        var field = undefined;

        if (panel) {
            var fieldArray = _getFieldsByName(panel, fieldName);
            if (fieldArray
                    && fieldArray.length > 0) {
                field = fieldArray[0];
            }
        }

        return field;
    };

    return self;
}();