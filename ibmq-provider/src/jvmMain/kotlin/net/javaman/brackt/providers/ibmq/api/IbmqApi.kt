package net.javaman.brackt.providers.ibmq.api

import kotlinx.coroutines.runBlocking
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest

actual fun IbmqApi.logInWithTokenSync(request: LogInWithTokenRequest) = runBlocking { logInWithToken(request) }

actual fun IbmqApi.apiTokenSync(accessToken: String, userId: String) = runBlocking { apiToken(accessToken, userId) }

actual fun IbmqApi.networksSync(accessToken: String) = runBlocking { networks(accessToken) }

actual fun IbmqApi.backendsSync(accessToken: String) = runBlocking { backends(accessToken) }

actual fun IbmqApi.jobsSync(accessToken: String) = runBlocking { jobs(accessToken) }

actual fun IbmqApi.jobsLimitSync(
    accessToken: String,
    network: String,
    group: String,
    project: String,
    device: String
) = runBlocking { jobsLimit(accessToken, network, group, project, device) }

actual fun IbmqApi.runExperimentSync(
    accessToken: String,
    request: RunExperimentRequest,
    network: String,
    group: String,
    project: String
) = runBlocking { runExperiment(accessToken, request, network, group, project) }

actual fun IbmqApi.versionsSync(
    accessToken: String,
    request: VersionsRequest,
    code: String
) = runBlocking { versions(accessToken, request, code) }
