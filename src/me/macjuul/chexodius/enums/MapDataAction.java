package me.macjuul.chexodius.enums;

import com.laytonsmith.PureUtilities.Common.StringUtils;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.CRE.CREIllegalArgumentException;

public enum MapDataAction {
    /*
     * Sets the scale of the canvas
     */
    SET_SCALE,
    
    /*
     * Returns the scale of the canvas
     */
    GET_SCALE,
    
    /*
     * Converts a 2D CArray matrix to a map canvas
     */
    DRAW,
    
    /*
     * Allow a map to update itself
     */
    UPDATE,
    
    /*
     * Get the world associated with the map
     */
    GET_WORLD,
    
    /*
     * Set the world the map is associated with
     */
    SET_WORLD,
    
    /*
     * Get the center
     */
    GET_CENTER,
    
    /*
     * Set the center
     */
    SET_CENTER,
    
    /*
     * Update cursors
     */
    DRAW_CURSORS;
    
    public static MapDataAction get(String s, Target t) {
        MapDataAction a;
        
        try {
            a = MapDataAction.valueOf(s);
        } catch(IllegalArgumentException e) {
            throw new CREIllegalArgumentException("The value of action can only be one of " + StringUtils.Join(MapDataAction.values(), ", ", " or "), t);
        }
        
        return a;
    }
}
