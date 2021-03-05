package edu.iastate.ece.sd.sdmay2126.application;


import java.util.LinkedList;


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
    private float maxPhosphateUptake;
    private float maxSulfurUptake;
    private float maxOxygenUptake;
    private String reactionToMaximize;
    private float expressionThreshold;
    private float expressionUncertainty;

    private LinkedList<String> geneKnockouts;
    private LinkedList<String> reactionKnockouts;
    private LinkedList<String> customFluxBounds;

    private String mediaSupplements;
    private String expressionCondition;
    // TODO customFluxBounds
    // TODO mediaSupplement
    // TODO expressionCondition

    public FBAParameters(boolean fluxVariabilityAnalysis, boolean minimizeFlux, boolean simulateAllSingleKos,
                         float activationCoefficient, float maxCarbonUptake, float maxNitrogenUptake,
                         float maxPhosphateUptake, float maxSulfurUptake, float maxOxygenUptake,
                         String reactionToMaximize, float expressionThreshold, float expressionUncertainty,
                         String geneKnockouts, LinkedList<String> customFluxBounds) {
        this.fluxVariabilityAnalysis = fluxVariabilityAnalysis;
        this.minimizeFlux = minimizeFlux;
        this.simulateAllSingleKos = simulateAllSingleKos;
        this.activationCoefficient = activationCoefficient;
        this.maxCarbonUptake = maxCarbonUptake;
        this.maxNitrogenUptake = maxNitrogenUptake;
        this.maxPhosphateUptake = maxPhosphateUptake;
        this.maxSulfurUptake = maxSulfurUptake;
        this.maxOxygenUptake = maxOxygenUptake;
        this.reactionToMaximize = reactionToMaximize;
        this.expressionThreshold = expressionThreshold;
        this.expressionUncertainty = expressionUncertainty;
        this.customFluxBounds = customFluxBounds;
        // TODO add other parameters
    }

    public FBAParameters(boolean fluxVariabilityAnalysis) {
        this.fluxVariabilityAnalysis = fluxVariabilityAnalysis;
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

    public float getMaxPhosphateUptake() {
        return maxPhosphateUptake;
    }

    public void setMaxPhosphateUptake(float maxPhosphateUptake) {
        this.maxPhosphateUptake = maxPhosphateUptake;
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

    public String getReactionToMaximize() {
        return reactionToMaximize;
    }

    public void setReactionToMaximize(String reactionToMaximize) {
        this.reactionToMaximize = reactionToMaximize;
    }


    public void setReactionKnockouts(LinkedList<String> reactionKnockouts) {
        this.reactionKnockouts = reactionKnockouts;
    }

    public LinkedList<String> getReactionKnockouts() {
        return reactionKnockouts;
    }

    public void setMediaSupplements(String mediaSupplements) {
        this.mediaSupplements = mediaSupplements;
    }

    public String getMediaSupplements() {
        return mediaSupplements;
    }

    public LinkedList<String> getGeneKnockouts() {
        return geneKnockouts;
    }

    public void setGeneKnockouts(LinkedList<String> geneKnockouts) {
        this.geneKnockouts = geneKnockouts;
    }

    public LinkedList<String> getCustomFluxBounds() {
        return customFluxBounds;
    }

    public void setCustomFluxBounds(LinkedList<String> customFluxBounds) {
        this.customFluxBounds = customFluxBounds;

    public void setExpressionCondition(String expressionCondition) {
        this.expressionCondition = expressionCondition;
    }

    public String getExpressionCondition() {
        return  expressionCondition;
    }

    @Override
    public ApplicationType getApplication() {
        return ApplicationType.FBA;
    }
}
