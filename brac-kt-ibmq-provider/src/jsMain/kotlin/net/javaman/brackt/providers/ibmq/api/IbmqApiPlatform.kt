@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:JsExport

package net.javaman.brackt.providers.ibmq.api

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest

fun IbmqApi.logInWithTokenAsync(request: LogInWithTokenRequest) = GlobalScope.promise { logInWithToken(request) }

fun IbmqApi.apiTokenAsync(accessToken: String, userId: String) = GlobalScope.promise { apiToken(accessToken, userId) }

fun IbmqApi.networksAsync(accessToken: String) = GlobalScope.promise { networks(accessToken) }

fun IbmqApi.backendsAsync(accessToken: String) = GlobalScope.promise { backends(accessToken) }

fun IbmqApi.jobsAsync(accessToken: String) = GlobalScope.promise { jobs(accessToken) }

fun IbmqApi.jobsLimitAsync(
    accessToken: String,
    network: String,
    group: String,
    project: String,
    device: String
) = GlobalScope.promise { jobsLimit(accessToken, network, group, project, device) }

fun IbmqApi.runExperimentAsync(
    accessToken: String,
    request: RunExperimentRequest,
    network: String,
    group: String,
    project: String
) = GlobalScope.promise { runExperiment(accessToken, request, network, group, project) }

fun IbmqApi.versionsAsync(
    accessToken: String,
    request: VersionsRequest,
    code: String
) = GlobalScope.promise { versions(accessToken, request, code) }

fun IbmqApi.latestAsync(accessToken: String, codeId: String) = GlobalScope.promise { latest(accessToken, codeId) }

fun IbmqApi.lastestAsync(accessToken: String, userId: String) = GlobalScope.promise { lastest(accessToken, userId) }

fun IbmqApi.newCodeAsync(accessToken: String, request: NewRequest) =
    GlobalScope.promise { newCode(accessToken, request) }

fun IbmqApi.jobAsync(accessToken: String, jobId: String) = GlobalScope.promise { job(accessToken, jobId) }

fun IbmqApi.resultDownloadUrlAsync(
    accessToken: String,
    jobId: String
) = GlobalScope.promise { resultDownloadUrl(accessToken, jobId) }
