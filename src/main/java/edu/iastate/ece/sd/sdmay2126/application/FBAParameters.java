package edu.iastate.ece.sd.sdmay2126.application;

/**
 * Simulation parameters for Flux Balance Analysis.
 */
public class FBAParameters implements ApplicationParameters {
    private boolean fluxVariabilityAnalysis;
    private boolean minimizeFlux;
    private boolean simulateAllSingleKos;
    private float activationCoefficient;
    private float maxCarbonUptake;
    private float maxNitrogenUptake;
    private float maxSulfurUptake;
    private float maxOxygenUptake;
    private String reactionToMaximize;
    private float expressionThreshold;
    private float expressionUncertainty;
    private String geneKnockouts;
    // TODO geneKnockouts
    // TODO reactionKnockouts
    // TODO customFluxBounds
    // TODO mediaSupplement
    // TODO expressionCondition


    public FBAParameters(boolean fluxVariabilityAnalysis, boolean minimizeFlux, boolean simulateAllSingleKos,
                         float activationCoefficient, float maxCarbonUptake, float maxNitrogenUptake,
                         float maxSulfurUptake, float maxOxygenUptake, String reactionToMaximize,
                         float expressionThreshold, float expressionUncertainty, String geneKnockouts) {
        this.fluxVariabilityAnalysis = fluxVariabilityAnalysis;
        this.minimizeFlux = minimizeFlux;
        this.simulateAllSingleKos = simulateAllSingleKos;
        this.activationCoefficient = activationCoefficient;
        this.maxCarbonUptake = maxCarbonUptake;
        this.maxNitrogenUptake = maxNitrogenUptake;
        this.maxSulfurUptake = maxSulfurUptake;
        this.maxOxygenUptake = maxOxygenUptake;
        this.reactionToMaximize = reactionToMaximize;
        this.expressionThreshold = expressionThreshold;
        this.expressionUncertainty = expressionUncertainty;
        this.geneKnockouts = geneKnockouts;
        // TODO add other parameters
    }

    public boolean isFluxVariabilityAnalysis() {
        return fluxVariabilityAnalysis;
    }

    public void setFluxVariabilityAnalysis(boolean fluxVariabilityAnalysis) {
        this.fluxVariabilityAnalysis = fluxVariabilityAnalysis;
    }

    public boolean isMinimizeFlux() {
        return minimizeFlux;
    }

    public void setMinimizeFlux(boolean minimizeFlux) {
        this.minimizeFlux = minimizeFlux;
    }

    public boolean isSimulateAllSingleKos() {
        return simulateAllSingleKos;
    }

    public void setSimulateAllSingleKos(boolean simulateAllSingleKos) {
        this.simulateAllSingleKos = simulateAllSingleKos;
    }

    public float getActivationCoefficient() {
        return activationCoefficient;
    }

    public void setActivationCoefficient(float activationCoefficient) {
        this.activationCoefficient = activationCoefficient;
    }

    public float getMaxCarbonUptake() {
        return maxCarbonUptake;
    }

    public void setMaxCarbonUptake(float maxCarbonUptake) {
        this.maxCarbonUptake = maxCarbonUptake;
    }

    public float getMaxNitrogenUptake() {
        return maxNitrogenUptake;
    }

    public void setMaxNitrogenUptake(float maxNitrogenUptake) {
        this.maxNitrogenUptake = maxNitrogenUptake;
    }

    public float getMaxSulfurUptake() {
        return maxSulfurUptake;
    }

    public void setMaxSulfurUptake(float maxSulfurUptake) {
        this.maxSulfurUptake = maxSulfurUptake;
    }

    public float getMaxOxygenUptake() {
        return maxOxygenUptake;
    }

    public void setMaxOxygenUptake(float maxOxygenUptake) {
        this.maxOxygenUptake = maxOxygenUptake;
    }

    public float getExpressionThreshold() {
        return expressionThreshold;
    }

    public void setExpressionThreshold(float expressionThreshold) {
        this.expressionThreshold = expressionThreshold;
    }

    public float getExpressionUncertainty() {
        return expressionUncertainty;
    }

    public void setExpressionUncertainty(float expressionUncertainty) {
        this.expressionUncertainty = expressionUncertainty;
    }

    public void setReactionToMaximize(String reactionToMaximize) {
        this.reactionToMaximize = reactionToMaximize;
    }

    public String getReactionToMaximize() {
        return  reactionToMaximize;
    }

    public String getGeneKnockouts() {
        return geneKnockouts;
    }

    public void setGeneKnockouts(String geneKnockouts) {
        this.geneKnockouts = geneKnockouts;
    }

    @Override
    public ApplicationType getApplication() {
        return ApplicationType.FBA;
    }
}
