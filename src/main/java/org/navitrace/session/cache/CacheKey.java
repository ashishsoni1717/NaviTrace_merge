
package org.navitrace.session.cache;

import org.navitrace.model.BaseModel;

record CacheKey(Class<? extends BaseModel> clazz, long id) {
    CacheKey(BaseModel object) {
        this(object.getClass(), object.getId());
    }
}
