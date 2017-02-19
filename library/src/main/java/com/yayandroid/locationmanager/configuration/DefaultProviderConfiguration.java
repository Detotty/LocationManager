package com.yayandroid.locationmanager.configuration;

import com.yayandroid.locationmanager.constants.ProviderType;

public final class DefaultProviderConfiguration {

    private final long requiredTimeInterval;
    private final long requiredDistanceInterval;
    private final float acceptableAccuracy;
    private final long acceptableTimePeriod;
    private final long gpsWaitPeriod;
    private final long networkWaitPeriod;
    private final String gpsMessage;

    private DefaultProviderConfiguration(Builder builder) {
        this.requiredTimeInterval = builder.requiredTimeInterval;
        this.requiredDistanceInterval = builder.requiredDistanceInterval;
        this.acceptableAccuracy = builder.acceptableAccuracy;
        this.acceptableTimePeriod = builder.acceptableTimePeriod;
        this.gpsWaitPeriod = builder.gpsWaitPeriod;
        this.networkWaitPeriod = builder.networkWaitPeriod;
        this.gpsMessage = builder.gpsMessage;
    }

    // region Getters
    public long requiredTimeInterval() {
        return requiredTimeInterval;
    }

    public long requiredDistanceInterval() {
        return requiredDistanceInterval;
    }

    public float acceptableAccuracy() {
        return acceptableAccuracy;
    }

    public long acceptableTimePeriod() {
        return acceptableTimePeriod;
    }

    public boolean askForGPSEnable() {
        return gpsMessage != null && gpsMessage.length() > 0;
    }

    public String gpsMessage() {
        return gpsMessage;
    }

    public long gpsWaitPeriod() {
        return gpsWaitPeriod;
    }

    public long networkWaitPeriod() {
        return networkWaitPeriod;
    }
    // endregion


    public static class Builder {

        private long requiredTimeInterval = Defaults.LOCATION_INTERVAL;
        private long requiredDistanceInterval = Defaults.LOCATION_DISTANCE_INTERVAL;
        private float acceptableAccuracy = Defaults.MIN_ACCURACY;
        private long acceptableTimePeriod = Defaults.TIME_PERIOD;
        private long gpsWaitPeriod = Defaults.WAIT_PERIOD;
        private long networkWaitPeriod = Defaults.WAIT_PERIOD;
        private String gpsMessage = Defaults.EMPTY_STRING;

        /**
         * TimeInterval will be used while getting location from default location providers
         * It will define in which period updates need to be delivered and will be used only when
         * {@linkplain LocationConfiguration#keepTracking} is set to true.
         * Default is {@linkplain Defaults#LOCATION_INTERVAL}
         */
        public Builder requiredTimeInterval(long requiredTimeInterval) {
            if (requiredTimeInterval < 0)
                throw new IllegalArgumentException("Required time interval cannot be set to negative value.");

            this.requiredTimeInterval = requiredTimeInterval;
            return this;
        }

        /**
         * DistanceInterval will be used while getting location from default location providers
         * It will define in which distance changes that we should receive an update and will be used only when
         * {@linkplain LocationConfiguration#keepTracking} is set to true.
         * Default is {@linkplain Defaults#LOCATION_DISTANCE_INTERVAL}
         */
        public Builder requiredDistanceInterval(long requiredDistanceInterval) {
            if (requiredDistanceInterval < 0)
                throw new IllegalArgumentException("Required distance interval cannot be set to negative value.");

            this.requiredDistanceInterval = requiredDistanceInterval;
            return this;
        }

        /**
         * Minimum Accuracy that you seek location to be
         * Default is {@linkplain Defaults#MIN_ACCURACY}
         */
        public Builder acceptableAccuracy(float acceptableAccuracy) {
            if (acceptableAccuracy < 0)
                throw new IllegalArgumentException("Acceptable accuracy cannot be set to negative value.");

            this.acceptableAccuracy = acceptableAccuracy;
            return this;
        }

        /**
         * Indicates time period that can be count as usable location,
         * this needs to be considered such as "last 5 minutes"
         * Default is {@linkplain Defaults#TIME_PERIOD}
         */
        public Builder acceptableTimePeriod(long acceptableTimePeriod) {
            if (acceptableTimePeriod < 0)
                throw new IllegalArgumentException("Acceptable time period cannot be set to negative value.");

            this.acceptableTimePeriod = acceptableTimePeriod;
            return this;
        }

        /**
         * Indicates what to display to user while asking to turn GPS on.
         * If you do not set this, user will not be asked to enable GPS.
         */
        public Builder gpsMessage(String gpsMessage) {
            this.gpsMessage = gpsMessage;
            return this;
        }

        /**
         * Indicates waiting time period before switching to next possible provider.
         * Possible to set provider wait periods separately by passing providerType as one of the
         * {@linkplain ProviderType.Source} values.
         * Default values are {@linkplain Defaults#WAIT_PERIOD}
         */
        public Builder setWaitPeriod(@ProviderType.Source int providerType, long milliseconds) {
            if (milliseconds < 0)
                throw new IllegalArgumentException("Wait period cannot be set to negative value.");

            switch (providerType) {
                case ProviderType.GOOGLE_PLAY_SERVICES: {
                    throw new IllegalStateException("GooglePlayServices waiting time period should be set on "
                          + "GPServicesConfiguration");
                }
                case ProviderType.NETWORK: {
                    this.networkWaitPeriod = milliseconds;
                    break;
                }
                case ProviderType.GPS: {
                    this.gpsWaitPeriod = milliseconds;
                    break;
                }
                case ProviderType.DEFAULT_PROVIDERS: {
                    this.gpsWaitPeriod = milliseconds;
                    this.networkWaitPeriod = milliseconds;
                    break;
                }
                case ProviderType.NONE: {
                    // ignored
                }
            }
            return this;
        }

        public DefaultProviderConfiguration build() {
            return new DefaultProviderConfiguration(this);
        }
    }
}
