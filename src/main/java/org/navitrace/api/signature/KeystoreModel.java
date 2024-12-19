
package org.navitrace.api.signature;

import org.navitrace.model.BaseModel;
import org.navitrace.storage.StorageName;

@StorageName("tc_keystore")
public class KeystoreModel extends BaseModel {

    private byte[] publicKey;

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    private byte[] privateKey;

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

}
