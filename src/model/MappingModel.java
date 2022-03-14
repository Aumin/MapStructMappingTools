package model;

public class MappingModel {

    private String fieldName;

    private String fieldType;

    private String parentFieldName;

    public MappingModel() {

    }

    public MappingModel(String fieldName, String fieldType, String parentFieldName) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.parentFieldName = parentFieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public void setParentFieldName(String parentFieldName) {
        this.parentFieldName = parentFieldName;
    }

    public String getParentFieldName() {
        if ("root".equals(parentFieldName)) {
            return "";
        }
        return parentFieldName;
    }

    @Override
    public String toString() {
        return "{" +
                "fieldName:\"" + fieldName + '\"' +
                ", fieldType:\"" + fieldType + '\"' +
                '}';
    }
}
