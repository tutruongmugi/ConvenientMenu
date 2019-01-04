package com.example.nguyenhuutu.convenientmenu;

import java.util.HashMap;
import java.util.Map;

public class DishType {
    /**
     * Properties
     */
    private String typeName;
    private String typeId;

    /**
     * Constructor methods
     */
    public DishType(String _typeId, String _typeName) {
        this.typeId = _typeId;
        this.typeName = _typeName;
    }

    /**
     * Getter methods
     */
    public String getTypeId() {
        return this.typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Other methods
     */

    /**
     * createDishTypeId()
     *  - Create new id for DishType
     * @param idNum
     * @return String
     */
    public static String createDishTypeId(Integer idNum) {
        String newId;

        if (idNum < 10) {
            newId = String.format("DTYPE0%d", idNum);
        }
        else {
            newId = String.format("DTYPE%d", idNum);
        }

        return newId;
    }
    /**
     * loadDishType()
     *  - Load DishType
     * @param document
     * @return DishType
     */
    public static DishType loadDishType(Map<String, Object> document) {
        String _typeId = document.get("type_id").toString();
        String _typeName = document.get("type_name").toString();

        return new DishType(_typeId, _typeName);
    }

    /**
     * createDishTypeData()
     *  - Create DishType's data for query
     * @param _typeId
     * @param _typeName
     * @return Map<String, Object>
     */
    public static Map<String, Object> createDishTypeData(String _typeId, String _typeName) {
        Map<String, Object> document = new HashMap<>();

        document.put("type_id", _typeId);
        document.put("type_name", _typeName);

        return document;
    }
}
