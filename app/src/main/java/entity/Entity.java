package entity;

import util.StringUtil;

public abstract class Entity {
    @Override
    public String toString() {
        return StringUtil.toJSON(this);
    }
}
