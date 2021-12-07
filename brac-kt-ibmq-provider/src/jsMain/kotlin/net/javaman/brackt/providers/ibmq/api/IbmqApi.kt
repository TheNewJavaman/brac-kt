@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq.api

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest

@Suppress("TooManyFunctions")
@JsExport
actual class IbmqApi {
    private val ibmqApiImpl: IbmqApiImpl by injection()

    fun logInWithTokenAsync(request: LogInWithTokenRequest) =
        GlobalScope.promise { ibmqApiImpl.logInWithToken(request) }

    fun apiTokenAsync(
        accessToken: String,
        userId: String
    ) = GlobalScope.promise { ibmqApiImpl.apiToken(accessToken, userId) }

    fun networksAsync(accessToken: String) = GlobalScope.promise { ibmqApiImpl.networks(accessToken) }

    fun backendsAsync(accessToken: String) = GlobalScope.promise { ibmqApiImpl.backends(accessToken) }

    fun jobsAsync(accessToken: String) = GlobalScope.promise { ibmqApiImpl.jobs(accessToken) }

    fun jobsLimitAsync(
        accessToken: String,
        network: String,
        group: String,
        project: String,
        device: String
    ) = GlobalScope.promise { ibmqApiImpl.jobsLimit(accessToken, network, group, project, device) }

    fun runExperimentAsync(
        accessToken: String,
        request: RunExperimentRequest,
        network: String,
        group: String,
        project: String
    ) = GlobalScope.promise { ibmqApiImpl.runExperiment(accessToken, request, network, group, project) }

    fun versionsAsync(
        accessToken: String,
        request: VersionsRequest,
        code: String
    ) = GlobalScope.promise { ibmqApiImpl.versions(accessToken, request, code) }

    fun latestAsync(accessToken: String, codeId: String) =
        GlobalScope.promise { ibmqApiImpl.latest(accessToken, codeId) }

    fun lastestAsync(accessToken: String, userId: String) =
        GlobalScope.promise { ibmqApiImpl.lastest(accessToken, userId) }

    fun newCodeAsync(accessToken: String, request: NewRequest) =
        GlobalScope.promise { ibmqApiImpl.newCode(accessToken, request) }

    fun jobAsync(accessToken: String, jobId: String) = GlobalScope.promise { ibmqApiImpl.job(accessToken, jobId) }

    fun resultDownloadUrlAsync(
        accessToken: String,
        jobId: String
    ) = GlobalScope.promise { ibmqApiImpl.resultDownloadUrl(accessToken, jobId) }
}
