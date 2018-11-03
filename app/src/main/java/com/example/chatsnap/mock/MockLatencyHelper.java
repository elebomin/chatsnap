package com.example.chatsnap.mock;

public class MockLatencyHelper {

    private static final boolean SIMULATE_NETWORK_LATENCY = true;
    private static final long NETWORK_LATENCY_MS = 2000L;

    public static void simulateNetworkLatency() {
        try {
            if (SIMULATE_NETWORK_LATENCY) Thread.sleep(NETWORK_LATENCY_MS);
        } catch (InterruptedException e) {}
    }
}
