package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.effects.Effect;

import java.util.ArrayList;
import java.util.Map;

public class EffectFactory {
    private static final Map<String, Effect> elementMap = Map.ofEntries(
        Map.entry("STUN", new Effect((long)-1, "STUN", 0)),
        Map.entry("INVISIBLE", new Effect((long)-1, "INVISIBLE", 0)),
        Map.entry("POISON", new Effect((long)-1, "POISON", 5)),
        Map.entry("BLEED", new Effect((long)3, "BLEED", 5)),
        Map.entry("PULL", new Effect((long)0, "PULL", 0)),
        Map.entry("BLIND", new Effect((long)-1, "BLIND", 0)),
        Map.entry("KNOCKBACK", new Effect((long)0, "KNOCKBACK", 0)),
        Map.entry("REVERSE", new Effect((long)10, "REVERSE", 0)),
        Map.entry("REVIVAL", new Effect((long)0, "REVIVAL", 0)),
        Map.entry("UNDEAD", new Effect((long)2, "UNDEAD", 0)),
        Map.entry("CONTROL_IMMUNITY", new Effect((long)7, "CONTROL_IMMUNITY", 0)),
        Map.entry("WEAPON_LOCKED", new Effect((long)20, "WEAPON_LOCKED", 0)),
        Map.entry("BUFF_DAMAGE", new Effect((long)-1, "BUFF_DAMAGE", 0))
);


    public static Effect getEffects(String id){
        return elementMap.get(id);
    }
}


