package com.krux.hyperion.objects.aws

/**
 * AWS Data Pipeline database objects.
 *
 * Ref: http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-object-databases.html
 */
trait AdpDatabase extends AdpDataPipelineObject {

  /** The name of the logical database. */
  def databaseName: Option[String]

  /** The properties of the JDBC connections for this database. */
  def jdbcProperties: Option[String]

  /** The password to connect to the database. */
  def `*password`: String

  /** The user name to connect to the database. */
  def username: String
}

/**
 * Defines an Amazon Redshift database.
 *
 * @param clusterId The identifier provided by the user when the Amazon Redshift cluster was
 *   created. For example, if the endpoint for your Amazon Redshift cluster is
 *   mydb.example.us-east-1.redshift.amazonaws.com, the correct clusterId value is mydb. In the
 *   Amazon Redshift console, this value is "Cluster Name".
 * @param connectionString The JDBC endpoint for connecting to an Amazon Redshift instance owned by
 *   an account different than the pipeline.
 */
case class AdpRedshiftDatabase(
    id: String,
    name: Option[String],
    clusterId: String,
    connectionString: Option[String],
    databaseName: Option[String],
    jdbcProperties: Option[String],
    `*password`: String,
    username: String
  ) extends AdpDatabase {

  val `type` = "RedshiftDatabase"
}