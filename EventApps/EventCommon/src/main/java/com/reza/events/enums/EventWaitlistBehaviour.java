package com.reza.events.enums;

public enum EventWaitlistBehaviour {
    NONE,           // no waitlist — cap is hard
    AUTO_APPROVE,   // promote from waitlist automatically when space opens
    MANUAL          // organiser promotes manually
}
