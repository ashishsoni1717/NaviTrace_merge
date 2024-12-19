
package org.navitrace.web;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.inject.hk2.Hk2InjectionManagerFactory;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.internal.inject.InjectionManagerFactory;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.navitrace.Main;

import jakarta.annotation.Priority;

@Priority(20)
public class WebInjectionManagerFactory implements InjectionManagerFactory {

    private final InjectionManagerFactory originalFactory = new Hk2InjectionManagerFactory();

    private InjectionManager injectGuiceBridge(InjectionManager injectionManager) {
        var serviceLocator = injectionManager.getInstance(ServiceLocator.class);
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        var guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(Main.getInjector());
        return injectionManager;
    }

    @Override
    public InjectionManager create() {
        return injectGuiceBridge(originalFactory.create());
    }

    @Override
    public InjectionManager create(Object parent) {
        return injectGuiceBridge(originalFactory.create(parent));
    }
}
