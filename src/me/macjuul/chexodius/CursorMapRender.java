package me.macjuul.chexodius;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;

public class CursorMapRender extends MapRenderer {
    private CArray cursors;
    private List<String> rendered;
    private Target t;

    public CursorMapRender(CArray cursors, Target t) {
        this.cursors = cursors;
        this.rendered = new ArrayList<String>();
        this.t = t;
    }
    
    public void unlockRender() {
        this.rendered.clear();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(MapView v, MapCanvas cvs, Player p) {
        if(!this.rendered.contains(p.getName())) {
            this.rendered.add(p.getName());
            MapCursorCollection cs = new MapCursorCollection();
            
            for(int c = 0; c < cursors.size(); c++) {
                CArray cur = Static.getArray(cursors.get(c, t), t);
                
                if(!cur.containsKey("x") || !cur.containsKey("y")) {
                    throw new CREFormatException("A cursor array is expected to contain x and y", t);
                }
                
                byte rotation = 0;
                byte type = 0;
                
                if(cur.containsKey("rotation")) {
                    rotation = Static.getInt8(cur.get("rotation", t), t);
                }
                if(cur.containsKey("type")) {
                    type = Static.getInt8(cur.get("type", t), t);
                }
                
                cs.addCursor(Static.getInt32(cur.get("x", t), t), Static.getInt32(cur.get("y", t), t), rotation, type);
            }
            
            cvs.setCursors(cs);
        }
    }

}
