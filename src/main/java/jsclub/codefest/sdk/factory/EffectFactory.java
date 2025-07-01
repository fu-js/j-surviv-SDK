package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.effects.Effect;

import java.util.ArrayList;
import java.util.Map;

public class EffectFactory {
    private static final Map<String, Effect> elementMap = Map.ofEntries(
        Map.entry("STUN", new Effect(-1, "STUN", 0)),
        Map.entry("INVISIBLE", new Effect(-1, "INVISIBLE", 0)),
        Map.entry("POISON", new Effect(-1, "POISON", 5)),
        Map.entry("BLEED", new Effect(3, "BLEED", 5)),
        Map.entry("PULL", new Effect(0, "PULL", 0)),
        Map.entry("BLIND", new Effect(-1, "BLIND", 0)),
        Map.entry("KNOCKBACK", new Effect(0, "KNOCKBACK", 0)),
        Map.entry("REVERSE", new Effect(10, "REVERSE", 0)),
        Map.entry("REVIVAL", new Effect(0, "REVIVAL", 0)),
        Map.entry("UNDEAD", new Effect(2, "UNDEAD", 0)),
        Map.entry("CONTROL_IMMUNITY", new Effect(7, "CONTROL_IMMUNITY", 0))
);


    public static Effect getEffects(String id){
        return elementMap.get(id);
    }
}


