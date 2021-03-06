package edu.iastate.ece.sd.sdmay2126.input.file.InputObjects;

import edu.iastate.ece.sd.sdmay2126.application.FBAParameters;

import java.util.LinkedList;

/**
 * Class to parse directly from the JSON file. Can then generate the FBAParams used later in the application
 * to configure FBA jobs.
 */
public class FBAParametersJSONObject {
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
    private LinkedList<String> mediaSupplements;
    private LinkedList<String> customFluxBounds;
    private LinkedList<String> reactionKnockouts;
    private boolean deleteCard;
    private String expressionCondition;

    public FBAParametersJSONObject(boolean fluxVariabilityAnalysis, boolean minimizeFlux, boolean simulateAllSingleKos,
                                   float activationCoefficient, float maxCarbonUptake, float maxNitrogenUptake,
                                   float maxPhosphateUptake, float maxSulfurUptake, float maxOxygenUptake,
                                   String reactionToMaximize, float expressionThreshold,
                                   float expressionUncertainty, LinkedList<String> geneKnockouts,
                                   LinkedList<String> mediaSupplements,
                                   LinkedList<String> customFluxBounds, LinkedList<String> reactionKnockouts,
                                   boolean deleteCard, String expressionCondition) {
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
        this.geneKnockouts = geneKnockouts;
        this.mediaSupplements = mediaSupplements;
        this.customFluxBounds = customFluxBounds;
        this.reactionKnockouts = reactionKnockouts;
        this.deleteCard = deleteCard;
        this.expressionCondition = expressionCondition;
    }

    public FBAParameters generateFBAParameters() {
        return new FBAParameters(fluxVariabilityAnalysis, minimizeFlux, simulateAllSingleKos, activationCoefficient,
                maxCarbonUptake, maxNitrogenUptake, maxPhosphateUptake, maxSulfurUptake, maxOxygenUptake,
                reactionToMaximize, expressionThreshold, expressionUncertainty,
                geneKnockouts, mediaSupplements, customFluxBounds, reactionKnockouts, deleteCard, expressionCondition);
    }
}
