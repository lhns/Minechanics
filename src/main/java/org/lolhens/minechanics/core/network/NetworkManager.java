package org.lolhens.minechanics.core.network;

import static org.lolhens.minechanics.core.reference.Reference.MOD_ID;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkManager {
    public final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    public NetworkManager() {
        // these packages are send from the client to the server
        // networkWrapper.registerMessage(?1.class, ?1.class, 0, Side.SERVER);

        // these packages are send from the server to the client
        // networkWrapper.registerMessage(?2.class, ?2.class, 10, Side.CLIENT);
    }
}
