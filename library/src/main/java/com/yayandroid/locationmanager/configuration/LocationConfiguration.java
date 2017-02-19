package com.yayandroid.locationmanager.configuration;

import com.yayandroid.locationmanager.providers.permissionprovider.StubPermissionProvider;

public final class LocationConfiguration {

    private final boolean keepTracking;
    private final PermissionConfiguration permissionConfiguration;
    private final GPServicesConfiguration gpServicesConfiguration;
    private final DefaultProviderConfiguration defaultProviderConfiguration;

    private LocationConfiguration(Builder builder) {
        this.keepTracking = builder.keepTracking;
        this.permissionConfiguration = builder.permissionConfiguration;
        this.gpServicesConfiguration = builder.gpServicesConfiguration;
        this.defaultProviderConfiguration = builder.defaultProviderConfiguration;
    }

    // region Getters
    public boolean keepTracking() {
        return keepTracking;
    }

    public PermissionConfiguration permissionConfiguration() {
        return permissionConfiguration;
    }

    public GPServicesConfiguration gpServicesConfiguration() {
        return gpServicesConfiguration;
    }

    public DefaultProviderConfiguration defaultProviderConfiguration() {
        return defaultProviderConfiguration;
    }
    // endregion

    public static class Builder {

        private boolean keepTracking = Defaults.KEEP_TRACKING;
        private PermissionConfiguration permissionConfiguration;
        private GPServicesConfiguration gpServicesConfiguration;
        private DefaultProviderConfiguration defaultProviderConfiguration;

        /**
         * If you need to keep receiving location updates, then you need to set this as true.
         * Otherwise manager will be aborted after any location received.
         * Default is False.
         */
        public Builder keepTracking(boolean keepTracking) {
            this.keepTracking = keepTracking;
            return this;
        }

        /**
         * This configuration is required in order to configure Permission Request process.
         * If this is not set, then no permission will be requested from user and
         * if {@linkplain Defaults#LOCATION_PERMISSIONS} permissions are not granted already,
         * then getting location will fail silently.
         */
        public Builder askForPermission(PermissionConfiguration permissionConfiguration) {
            this.permissionConfiguration = permissionConfiguration;
            return this;
        }

        /**
         * This configuration is required in order to configure GooglePlayServices Api.
         * If this is not set, then GooglePlayServices will not be used.
         */
        public Builder useGooglePlayServices(GPServicesConfiguration gpServicesConfiguration) {
            this.gpServicesConfiguration = gpServicesConfiguration;
            return this;
        }

        /**
         * This configuration is required in order to configure Default Location Providers.
         * If this is not set, then they will not be used.
         */
        public Builder useDefaultProviders(DefaultProviderConfiguration defaultProviderConfiguration) {
            this.defaultProviderConfiguration = defaultProviderConfiguration;
            return this;
        }

        public LocationConfiguration build() {
            if (gpServicesConfiguration == null && defaultProviderConfiguration == null) {
                throw new IllegalStateException("You need to specify one of the provider configurations."
                      + " Please see GPServicesConfiguration and DefaultProviderConfiguration");
            }

            if (permissionConfiguration == null) {
                permissionConfiguration = new PermissionConfiguration.Builder()
                      .permissionProvider(new StubPermissionProvider())
                      .build();
            }

            return new LocationConfiguration(this);
        }

    }
}
