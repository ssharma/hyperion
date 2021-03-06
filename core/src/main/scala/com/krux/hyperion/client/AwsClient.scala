package com.krux.hyperion.client

import com.amazonaws.auth.{DefaultAWSCredentialsProviderChain, STSAssumeRoleSessionCredentialsProvider}
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.datapipeline.DataPipelineClient
import org.slf4j.LoggerFactory
import com.krux.hyperion.DataPipelineDefGroup
import com.krux.stubborn.Retryable
import com.krux.stubborn.policy.ExponentialBackoffAndJitter


trait AwsClient extends Retryable with ExponentialBackoffAndJitter {

  lazy val log = LoggerFactory.getLogger(getClass)

  def client: DataPipelineClient

  override def base: Int = 3000

  override def cap: Int = 24000 // theoretical max retry delay with 3 retries

}

object AwsClient {

  def getClient(regionId: Option[String] = None, roleArn: Option[String] = None)
    : DataPipelineClient = {

    val region: Region =
      Region.getRegion(regionId.map(r => Regions.fromName(r)).getOrElse(Regions.US_EAST_1))
    val defaultProvider =
      new DefaultAWSCredentialsProviderChain()
    val stsProvider =
      roleArn.map(new STSAssumeRoleSessionCredentialsProvider(defaultProvider, _, "hyperion"))
    new DataPipelineClient(stsProvider.getOrElse(defaultProvider)).withRegion(region)
  }

  def apply(
      pipelineDef: DataPipelineDefGroup,
      regionId: Option[String],
      roleArn: Option[String]
    ): AwsClientForDef =
    new AwsClientForDef(getClient(regionId, roleArn), pipelineDef)

}
