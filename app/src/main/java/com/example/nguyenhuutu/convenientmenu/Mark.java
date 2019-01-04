package com.example.nguyenhuutu.convenientmenu;

import java.util.HashMap;
import java.util.Map;

public class Mark {
    /**
     * Properties
     */
    private String username;
    private String refId;
    private boolean isRest;

    /**
     * Constructor Methods
     */
   public Mark(String _username, String _refId, boolean _isRest) {
        this.username = _username;
        this.refId = _refId;
        this.isRest = _isRest;
    }

    /**
     * Getter methods
     */
    public String getUsername() {
        return username;
    }

    public String getRefId() {
        return refId;
    }

    public boolean isRest() {
        return isRest;
    }

    /**
     * Other Methods
     */

    /**
     * createMarkId()
     *  - create a new id for Mark
     * @param idNum
     * @return String
     */
    public static String createMarkId(Integer idNum) {
        return String.format("MARK%d", idNum);
    }
    /**
     * loadMark()
     *  - Load Mark's information
     * @param document
     * @return Mark
     */
    public static Mark loadMark(Map<String, Object> document) {
        String _username = document.get("username").toString();
        String _refId = document.get("ref_id").toString();
        boolean _isRest = (boolean)document.get("is_rest");

        return new Mark(_username, _refId, _isRest);
    }

    /**
     * createMarkData()
     *  - Create Mark's data for query
     * @param _username
     * @param _refId
     * @param _isRest
     * @return Map<String, Object>
     */
    public static Map<String, Object> createMarkData(String _username, String _refId, boolean _isRest) {
        Map<String, Object> document = new HashMap<>();

        document.put("username", _username);
        document.put("ref_id", _refId);
        document.put("is_rest", _isRest);

        return document;
    }


}
