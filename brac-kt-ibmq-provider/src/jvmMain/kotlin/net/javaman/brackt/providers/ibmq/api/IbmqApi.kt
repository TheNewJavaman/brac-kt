package net.javaman.brackt.providers.ibmq.api

import kotlinx.coroutines.runBlocking
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest

@Suppress("TooManyFunctions")
actual class IbmqApi {
    private val ibmqApiImpl: IbmqApiImpl by injection()

    fun logInWithTokenSync(request: LogInWithTokenRequest) = runBlocking { ibmqApiImpl.logInWithToken(request) }

    fun apiTokenSync(accessToken: String, userId: String) = runBlocking { ibmqApiImpl.apiToken(accessToken, userId) }

    fun networksSync(accessToken: String) = runBlocking { ibmqApiImpl.networks(accessToken) }

    fun backendsSync(accessToken: String) = runBlocking { ibmqApiImpl.backends(accessToken) }

    fun jobsSync(accessToken: String) = runBlocking { ibmqApiImpl.jobs(accessToken) }

    fun jobsLimitSync(
        accessToken: String,
        network: String,
        group: String,
        project: String,
        device: String
    ) = runBlocking { ibmqApiImpl.jobsLimit(accessToken, network, group, project, device) }

    fun runExperimentSync(
        accessToken: String,
        request: RunExperimentRequest,
        network: String,
        group: String,
        project: String
    ) = runBlocking { ibmqApiImpl.runExperiment(accessToken, request, network, group, project) }

    fun versionsSync(
        accessToken: String,
        request: VersionsRequest,
        code: String
    ) = runBlocking { ibmqApiImpl.versions(accessToken, request, code) }

    fun latestSync(accessToken: String, codeId: String) = runBlocking { ibmqApiImpl.latest(accessToken, codeId) }

    fun lastestSync(accessToken: String, userId: String) = runBlocking { ibmqApiImpl.lastest(accessToken, userId) }

    fun newCodeSync(
        accessToken: String,
        request: NewRequest
    ) = runBlocking { ibmqApiImpl.newCode(accessToken, request) }

    fun jobSync(accessToken: String, jobId: String) = runBlocking { ibmqApiImpl.job(accessToken, jobId) }

    fun resultDownloadUrlSync(
        accessToken: String,
        jobId: String
    ) = runBlocking { ibmqApiImpl.resultDownloadUrl(accessToken, jobId) }
}
