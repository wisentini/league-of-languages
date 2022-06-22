package entity;

import util.StringUtil;

public abstract class Entity {
    @Override
    public String toString() {
        String string = StringUtil.toJSON(this);
        return string;
    }
}
