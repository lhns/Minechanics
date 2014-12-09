package com.dafttech.minechanics.client;

import com.dafttech.minechanics.CommonProxy;

public class ClientProxy extends CommonProxy {
    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void registerRenderers() {
    }
}
