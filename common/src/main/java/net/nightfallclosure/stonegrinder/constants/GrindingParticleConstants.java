package net.nightfallclosure.stonegrinder.constants;

public class GrindingParticleConstants {
    public static final double[][] particleXZOffsets = {
            {0.1D, 0.5D},
            {0.9D, 0.5D},
            {0.5D, 0.1D},
            {0.5D, 0.9D}
    };

    public static final double[][] particleXZDeltas = {
            {-2.1D, 0.0D},
            {2.1D, 0.0D},
            {0.0D, -2.1D},
            {0.0D, 2.1D}
    };

    public static final double[][] particleXZRandomOffsetVectors = {
            {0.0D, -1.0D},
            {0.0D, 1.0D},
            {1.0D, 0.0D},
            {-1.0D, 0.0D}
    };
}
