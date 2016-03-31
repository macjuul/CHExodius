package me.macjuul.chexodius;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;

public class CanvasMapRender extends MapRenderer {
    private CArray grid;
    private List<String> rendered;
    private Target t;

    public CanvasMapRender(CArray canvas, Target t) {
        this.grid = canvas;
        this.rendered = new ArrayList<String>();
        this.t = t;
    }
    
    public void unlockRender() {
        this.rendered.clear();
        Bukkit.broadcastMessage("Unlocked");
    }

    @Override
    public void render(MapView v, MapCanvas cvs, Player p) {
        if(!this.rendered.contains(p.getName())) {
            this.rendered.add(p.getName());
            
            Bukkit.broadcastMessage("laars");
            
            for(Construct px : grid.keySet()) {
                String[] split = px.val().split("-");
                byte c = UtilClass.getMapColor(new CInt(grid.get(px, t).val(), t));
                cvs.setPixel(Integer.valueOf(split[0]), Integer.valueOf(split[1]), c);
            }
            
            p.sendMap(v);
        }
    }

}
