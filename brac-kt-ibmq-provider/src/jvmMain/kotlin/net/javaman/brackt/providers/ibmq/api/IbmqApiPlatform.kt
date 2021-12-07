package net.javaman.brackt.providers.ibmq.api

import kotlinx.coroutines.runBlocking
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest

fun IbmqApi.logInWithTokenSync(request: LogInWithTokenRequest) = runBlocking { logInWithToken(request) }

fun IbmqApi.apiTokenSync(accessToken: String, userId: String) = runBlocking { apiToken(accessToken, userId) }

fun IbmqApi.networksSync(accessToken: String) = runBlocking { networks(accessToken) }

fun IbmqApi.backendsSync(accessToken: String) = runBlocking { backends(accessToken) }

fun IbmqApi.jobsSync(accessToken: String) = runBlocking { jobs(accessToken) }

fun IbmqApi.jobsLimitSync(
    accessToken: String,
    network: String,
    group: String,
    project: String,
    device: String
) = runBlocking { jobsLimit(accessToken, network, group, project, device) }

fun IbmqApi.runExperimentSync(
    accessToken: String,
    request: RunExperimentRequest,
    network: String,
    group: String,
    project: String
) = runBlocking { runExperiment(accessToken, request, network, group, project) }

fun IbmqApi.versionsSync(
    accessToken: String,
    request: VersionsRequest,
    code: String
) = runBlocking { versions(accessToken, request, code) }

fun IbmqApi.latestSync(accessToken: String, codeId: String) = runBlocking { latest(accessToken, codeId) }

fun IbmqApi.lastestSync(accessToken: String, userId: String) = runBlocking { lastest(accessToken, userId) }

fun IbmqApi.newCodeSync(accessToken: String, request: NewRequest) = runBlocking { newCode(accessToken, request) }

fun IbmqApi.jobSync(accessToken: String, jobId: String) = runBlocking { job(accessToken, jobId) }

fun IbmqApi.resultDownloadUrlSync(
    accessToken: String,
    jobId: String
) = runBlocking { resultDownloadUrl(accessToken, jobId) }
