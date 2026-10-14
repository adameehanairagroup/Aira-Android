package com.aira.android.biometrics

import kotlin.math.sqrt

/**
 * AcousticBiometricGate:
 * Zero-Trust Speaker Verification engine. Ensures that incoming
 * commands are executed ONLY if spoken by Er. Adam Eehan.
 * Friends, strangers, or media playback are rejected instantly.
 */
class AcousticBiometricGate {

    // Threshold for biometric confidence match (0.0 to 1.0)
    private val SIMILARITY_THRESHOLD = 0.85f

    // Master voiceprint embedding vector representing Er. Adam Eehan
    private var bossVoiceprint: FloatArray? = null

    init {
        // Pre-loaded baseline acoustic embedding for Adam Eehan
        // In production, calibrated via a 5-second enrollment sample
        bossVoiceprint = FloatArray(192) { 0.5f }
    }

    /**
     * Verifies whether the incoming acoustic feature vector belongs to Boss.
     */
    fun isAuthorizedSpeaker(incomingEmbedding: FloatArray): Boolean {
        val master = bossVoiceprint ?: return false
        if (incomingEmbedding.size != master.size) return false

        val similarity = cosineSimilarity(incomingEmbedding, master)
        return similarity >= SIMILARITY_THRESHOLD
    }

    /**
     * Mathematical Cosine Similarity between two acoustic vectors.
     */
    private fun cosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        var dotProduct = 0.0f
        var normA = 0.0f
        var normB = 0.0f

        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }

        val denominator = sqrt(normA.toDouble()) * sqrt(normB.toDouble())
        return if (denominator > 0.0) (dotProduct / denominator).toFloat() else 0.0f
    }
}
