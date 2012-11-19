/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MetadataCore;

import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;

/**
 *
 * @author ivano
 */
public class MetadataConstants {
    
    // <editor-fold desc="Constants">
    public static String sLocationSaveAlert = "D:\\Alert onlogija\\alert.owl";
    public static String sLocationLoadAlert = "file:D:/Alert onlogija/alert.owl";
    public static String sActiveMQAddress = "tcp://www.cimcollege.rs:61616";
    public static String sBackupFilesLocation = "";
    public static String sLogFilesLocation = "";
       
    public static String c_http = "http://";
    
    // <editor-fold desc="Event types">
    public static String c_ET_ALERT_KESI_IssueNew = "ALERT.KESI.IssueNew";
    public static String c_ET_ALERT_KESI_IssueUpdate = "ALERT.KESI.IssueUpdate";
    public static String c_ET_ALERT_Metadata_IssueNew_Stored = "ALERT.Metadata.IssueNew.Stored";
    public static String c_ET_ALERT_Metadata_IssueUpdate_Stored = "ALERT.Metadata.IssueUpdate.Stored";
    //public static String c_ET_person_requestNew = "Metadata.person.requestNew";
    //public static String c_ET_person_requestUpdate = "Metadata.person.requestUpdate";
    //public static String c_ET_person_replyNewUpdate = "Metadata.person.replyNewUpdate";
    public static String c_ET_ALERT_KESI_CommitNew = "ALERT.KESI.CommitNew";
    public static String c_ET_ALERT_Metadata_CommitNew_Stored = "ALERT.Metadata.CommitNew.Stored";
    //public static String c_ET_APICall_request = "Metadata.APICall.request";
    public static String c_ET_ALERT_KESI_APICallRequest = "ALERT.KESI.APICallRequest";
    public static String c_ET_ALERT_KEUI_APICallRequest = "ALERT.KEUI.APICallRequest";
    public static String c_ET_ALERT_STARDOM_APICallRequest = "ALERT.STARDOM.APICallRequest";
    public static String c_ET_ALERT_Panteon_APICallRequest = "ALERT.Panteon.APICallRequest";
    public static String c_ET_ALERT_UI_APICallRequest = "ALERT.UI.APICallRequest";
    public static String c_ET_ALERT_Search_APICallRequest = "ALERT.Search.APICallRequest";
    //public static String c_ET_APICall_reply = "Metadata.APICall.reply";
    public static String c_ET_ALERT_Metadata_APICallResponse = "ALERT.Metadata.APICallResponse";
    public static String c_ET_member_request = "Metadata.member.request";
    public static String c_ET_member_reply = "Metadata.member.reply";
    public static String c_ET_ALERT_KEUI_IssueNew_Annotated = "ALERT.KEUI.IssueNew.Annotated";
    public static String c_ET_ALERT_Metadata_IssueNew_Updated = "ALERT.Metadata.IssueNew.Updated";
    public static String c_ET_ALERT_KEUI_IssueUpdate_Annotated = "ALERT.KEUI.IssueUpdate.Annotated";
    public static String c_ET_ALERT_Metadata_IssueUpdate_Updated = "ALERT.Metadata.IssueUpdate.Updated";
    public static String c_ET_ALERT_KEUI_CommitNew_Annotated = "ALERT.KEUI.CommitNew.Annotated";
    public static String c_ET_ALERT_Metadata_CommitNew_Updated = "ALERT.Metadata.CommitNew.Updated";
    public static String c_ET_ALERT_KEUI_ForumPostNew_Annotated = "ALERT.KEUI.ForumPostNew.Annotated";
    public static String c_ET_ALERT_Metadata_ForumPostNew_Updated = "ALERT.Metadata.ForumPostNew.Updated";
    public static String c_ET_ALERT_KEUI_MailNew_Annotated = "ALERT.KEUI.MailNew.Annotated";
    public static String c_ET_ALERT_Metadata_MailNew_Updated = "ALERT.Metadata.MailNew.Updated";
    public static String c_ET_ALERT_KEUI_WikiPostNew_Annotated = "ALERT.KEUI.WikiPostNew.Annotated";
    public static String c_ET_ALERT_WikiSensor_WikiPostNew = "ALERT.WikiSensor.WikiPostNew";
    public static String c_ET_ALERT_ForumSensor_ForumPostNew = "ALERT.ForumSensor.ForumPostNew";
    public static String c_ET_ALERT_Metadata_ForumPostNew_Stored = "ALERT.Metadata.ForumPostNew.Stored";
    public static String c_ET_ALERT_MLSensor_MailNew = "ALERT.MLSensor.MailNew";
    public static String c_ET_ALERT_Metadata_MailNew_Stored = "ALERT.Metadata.MailNew.Stored";
    public static String c_ET_ALERT_WikiSensor_ArticleAdded = "ALERT.WikiSensor.ArticleAdded";
    public static String c_ET_ALERT_Metadata_ArticleAdded_Stored = "ALERT.Metadata.ArticleAdded.Stored";
    public static String c_ET_ALERT_WikiSensor_ArticleModified = "ALERT.WikiSensor.ArticleModified";
    public static String c_ET_ALERT_Metadata_ArticleModified_Stored = "ALERT.Metadata.ArticleModified.Stored";
    public static String c_ET_ALERT_WikiSensor_ArticleDeleted = "ALERT.WikiSensor.ArticleDeleted";
    public static String c_ET_ALERT_Metadata_ArticleDeleted_Stored = "ALERT.Metadata.ArticleDeleted.Stored";
    public static String c_ET_ALERT_STARDOM_CompetencyNew = "ALERT.STARDOM.CompetencyNew";
    public static String c_ET_ALERT_STARDOM_CompetencyUpdate = "ALERT.STARDOM.CompetencyUpdate";
    public static String c_ET_ALERT_Metadata_CompetencyNew_Stored = "ALERT.Metadata.CompetencyNew.Stored";
    public static String c_ET_ALERT_Metadata_CompetencyUpdate_Stored = "ALERT.Metadata.CompetencyUpdate.Stored";
    public static String c_ET_ALERT_STARDOM_IdentityNew = "ALERT.STARDOM.IdentityNew";
    public static String c_ET_ALERT_STARDOM_IdentityUpdate = "ALERT.STARDOM.IdentityUpdate";
    public static String c_ET_ALERT_STARDOM_IdentityRemove = "ALERT.STARDOM.IdentityRemove";
    public static String c_ET_ALERT_Metadata_IdentityNew_Stored = "ALERT.Metadata.IdentityNew.Stored";
    public static String c_ET_ALERT_Metadata_IdentityUpdate_Stored = "ALERT.Metadata.IdentityUpdate.Stored";
    public static String c_ET_ALERT_Metadata_IdentityRemove_Stored = "ALERT.Metadata.IdentityRemove.Stored";
    public static String c_ET_ALERT_OCELOt_ConceptNew = "ALERT.OCELOt.ConceptNew";
    public static String c_ET_ALERT_Metadata_ConceptNew_Stored = "ALERT.Metadata.ConceptNew.Stored";
    
    // </editor-fold>
       
    // <editor-fold desc="XML elements">
    public static String c_XMLE_Id = "Id";
    public static String c_XMLE_Uri = "Uri";
    public static String c_XMLE_event = "event";
    public static String c_XMLE_head = "head";
    public static String c_XMLE_sender = "sender";
    public static String c_XMLE_timestamp = "timestamp";
    public static String c_XMLE_sequencenumber = "sequencenumber";
    public static String c_XMLE_startTime = "startTime";
    public static String c_XMLE_endTime = "endTime";
    public static String c_XMLE_eventName = "eventName";
    public static String c_XMLE_eventId = "eventId";
    public static String c_XMLE_eventType = "eventType";
    public static String c_XMLE_sentby = "sentby";
    
    public static String c_XMLE_kesi = "kesi";
    public static String c_XMLE_keui = "keui";
    public static String c_XMLE_mdservice = "mdservice";
    public static String c_XMLE_mlsensor = "mlsensor";
    public static String c_XMLE_rawTextualData = "rawTextualData";
    public static String c_XMLE_relatedOntologyRef = "relatedOntologyRef";
    public static String c_XMLE_content = "content";
    
    public static String c_XMLE_ontoProperty = "onto:property";
    public static String c_XMLE_name = "name";
    public static String c_XMLE_typeOf = "typeOf";
    public static String c_XMLE_value = "value";
    public static String c_XMLE_class = "class";
    public static String c_XMLE_property = "property";
    
    public static String c_XMLE_payload = "payload";
    public static String c_XMLE_meta = "meta";
    public static String c_XMLE_eventData = "eventData";
    
    public static String c_XMLE_refersToRequest = "refersToRequest";
    public static String c_XMLE_requestEventId = "requestEventId";
    public static String c_XMLE_responseData = "responseData";
    public static String c_XMLE_item = "item";
    public static String c_XMLE_isRelatedTo = "isRelatedTo";
       
    public static String c_XMLE_StringType = "http://www.w3.org/TR/xmlschema-2/#string";
    
    public static String c_XMLE_BugType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#Bug";
    
    
    public static String c_XMLE_Removed = "Removed";
    
    public static String c_XMLE_issue = "issue";
    public static String c_XMLE_issueAuthor = "issueAuthor";
    public static String c_XMLE_issueStatus = "issueStatus";
    public static String c_XMLE_issueResolution = "issueResolution";
    public static String c_XMLE_issueDescription = "issueDescription";
    public static String c_XMLE_issueKeyword = "issueKeyword";
    public static String c_XMLE_issueNumber = "issueNumber";
    public static String c_XMLE_issueProduct = "issueProduct";
    public static String c_XMLE_product = "product";
    public static String c_XMLE_productComponent = "productComponent";
    public static String c_XMLE_productVersion = "productVersion";
    public static String c_XMLE_issueComputerSystem = "issueComputerSystem";
    public static String c_XMLE_computerSystem = "computerSystem";
    public static String c_XMLE_computerSystemPlatform = "computerSystemPlatform";
    public static String c_XMLE_computerSystemOS = "computerSystemOS";
    public static String c_XMLE_issuePriority = "issuePriority";
    public static String c_XMLE_issueSeverity = "issueSeverity";
    public static String c_XMLE_issueAssignedTo = "issueAssignedTo";
    public static String c_XMLE_issueCCPerson = "issueCCPerson";
    public static String c_XMLE_issueUrl = "issueUrl";
    public static String c_XMLE_issueDependsOn = "issueDependsOn";
    public static String c_XMLE_issueBlocks = "issueBlocks";
    public static String c_XMLE_issueDuplicateOf = "issueDuplicateOf";
    public static String c_XMLE_issueMergedInto = "issueMergedInto";
    public static String c_XMLE_issueDateOpened = "issueDateOpened";
    public static String c_XMLE_issueLastModified = "issueLastModified";
    public static String c_XMLE_issueMilestone = "issueMilestone";
    public static String c_XMLE_milestone = "milestone";
    public static String c_XMLE_milestoneTarget = "milestoneTarget";
    public static String c_XMLE_issueComment = "issueComment";
    public static String c_XMLE_comment = "comment";
    public static String c_XMLE_commentNumber = "commentNumber";
    public static String c_XMLE_commentText = "commentText";
    public static String c_XMLE_commentPerson = "commentPerson";
    public static String c_XMLE_commentDate = "commentDate";
    public static String c_XMLE_issueAttachment = "issueAttachment";
    public static String c_XMLE_attachment = "attachment";
    public static String c_XMLE_attachmentFilename = "attachmentFilename";
    public static String c_XMLE_attachmentType = "attachmentType";
    public static String c_XMLE_attachmentCreator = "attachmentCreator";
    public static String c_XMLE_issueActivity = "issueActivity";
    public static String c_XMLE_activity = "activity";
    public static String c_XMLE_activityWho = "activityWho";
    public static String c_XMLE_activityWhen = "activityWhen";
    public static String c_XMLE_activityWRA = "activityWRA";
    public static String c_XMLE_activityWhat = "activityWhat";
    public static String c_XMLE_activityRemoved = "activityRemoved";
    public static String c_XMLE_activityAdded = "activityAdded";
    public static String c_XMLE_issueTracker = "issueTracker";
    public static String c_XMLE_issueTrackerType = "issueTrackerType";
    public static String c_XMLE_issueTrackerURL = "issueTrackerURL";
    
    public static String c_XMLE_issueAnnotationStatus = "issueAnnotationStatus";
    
    public static String c_XMLE_commit = "commit";
    public static String c_XMLE_commitRepository = "commitRepository";
    public static String c_XMLE_commitRevisionTag = "commitRevisionTag";
    public static String c_XMLE_commitAuthor = "commitAuthor";
    public static String c_XMLE_commitCommtter = "commitCommitter";
    public static String c_XMLE_commitDate = "commitDate";
    public static String c_XMLE_commitMessageLog = "commitMessageLog";
    public static String c_XMLE_commitFileNumber = "commitFileNumber";
    
    public static String c_XMLE_message = "message";
    public static String c_XMLE_from = "from";
    public static String c_XMLE_date = "date";
    public static String c_XMLE_inReplyTo = "inReplyTo";
    public static String c_XMLE_references = "references";
    public static String c_XMLE_attachments = "attachments";
    
    public static String c_XMLE_wikiSensor = "wikiSensor";
    public static String c_XMLE_source = "source";
    public static String c_XMLE_url = "url";
    public static String c_XMLE_title = "title";
    public static String c_XMLE_rawText = "rawText";
    public static String c_XMLE_user = "user";
    public static String c_XMLE_isMinor = "isMinor";
    
    public static String c_XMLE_person = "person";
    public static String c_XMLE_persons = "persons";
    public static String c_XMLE_personFirstName = "personFirstName";
    public static String c_XMLE_personLastName = "personLastName";
    public static String c_XMLE_personName = "personName";
    public static String c_XMLE_personGender = "personGender";
    public static String c_XMLE_personEmail = "personEmail";
    
    public static String c_XMLE_apiRequest = "apiRequest";
    public static String c_XMLE_apiResponse = "apiResponse";
    public static String c_XMLE_apiCall = "apiCall";
    public static String c_XMLE_requestData = "requestData";
    public static String c_XMLE_inputParameter = "inputParameter";
    
    public static String c_XMLE_QueryBugPerson_isRelatedTo = "isRelatedTo";
    public static String c_XMLE_QueryBugPerson_hasID = "hasID";
    
    public static String c_XMLE_email = "email";
    public static String c_XMLE_firstname = "firstname";
    public static String c_XMLE_lastname = "lastname";
    public static String c_XMLE_username = "username";
    public static String c_XMLE_id = "id";
    public static String c_XMLE_editCount = "editCount";
       
    public static String c_XMLE_result = "result";
    public static String c_XMLE_post = "post";
    public static String c_XMLE_wikiPage = "wikiPage";
    public static String c_XMLE_method = "method";
    
    public static String c_XMLE_annotation = "annotation";
    public static String c_XMLE_annotations = "annotations";
    public static String c_XMLE_itemUri = "itemUri";
    public static String c_XMLE_issueDescriptionAnnotated = "issueDescriptionAnnotated";
    public static String c_XMLE_commentTextAnnotated = "commentTextAnnotated";
    public static String c_XMLE_commitMessageLogAnnotated = "commitMessageLogAnnotated";
    public static String c_XMLE_subjectAnnotated = "subjectAnnotated";
    public static String c_XMLE_bodyAnnotated = "bodyAnnotated";
    public static String c_XMLE_contentAnnotated = "contentAnnotated";
    public static String c_XMLE_issueDescriptionConcepts = "issueDescriptionConcepts";
    public static String c_XMLE_commentTextConcepts = "commentTextConcepts";
    public static String c_XMLE_commitMessageLogConcepts = "commitMessageLogConcepts";
    public static String c_XMLE_subjectConcepts = "subjectConcepts";
    public static String c_XMLE_bodyConcepts = "bodyConcepts";
    public static String c_XMLE_contentConcepts = "contentConcepts";
    public static String c_XMLE_concept = "concept";
    public static String c_XMLE_weight = "weight";
    public static String c_XMLE_uri = "uri";
    public static String c_XMLE_itemId = "itemId";
    public static String c_XMLE_threadId = "threadId";
    public static String c_XMLE_referenceUri = "referenceUri";

    public static String c_XMLE_forumSensor = "forumSensor";
    public static String c_XMLE_forum = "forum";
    public static String c_XMLE_forumPost = "forumPost";
    public static String c_XMLE_thread = "thread";
    public static String c_XMLE_forumThreadId = "forumThreadId";
    public static String c_XMLE_forumId = "forumId";
    public static String c_XMLE_forumName = "forumName";
    public static String c_XMLE_forumItemId = "forumItemId";
    public static String c_XMLE_postId = "postId";
    public static String c_XMLE_time = "time";
    public static String c_XMLE_subject = "subject";
    public static String c_XMLE_body = "body";
    public static String c_XMLE_author = "author";
    public static String c_XMLE_category = "category";
    public static String c_XMLE_forumPostSubject = "forumPostSubject";
    public static String c_XMLE_forumPostAuthor = "forumPostAuthor";
    public static String c_XMLE_forumPostTime = "forumPostTime";
    public static String c_XMLE_forumPostCategory = "forumPostCategory";
    
    public static String c_XMLE_commitFile = "commitFile";
    public static String c_XMLE_file = "file";
    public static String c_XMLE_fileAction = "fileAction";
    public static String c_XMLE_fileBranch = "fileBranch";
    public static String c_XMLE_fileName = "fileName";
    public static String c_XMLE_fileModules = "fileModules";
    public static String c_XMLE_module = "module";
    public static String c_XMLE_moduleName = "moduleName";
    public static String c_XMLE_moduleStartLine = "moduleStartLine";
    public static String c_XMLE_moduleEndLine = "moduleEndLine";
    public static String c_XMLE_moduleMethods = "moduleMethods";
    public static String c_XMLE_methodName = "methodName";
    public static String c_XMLE_methodStartLine = "methodStartLine";
    public static String c_XMLE_methodEndLine = "methodEndLine";
    public static String c_XMLE_methodIssue = "methodIssue";
    public static String c_XMLE_commitProduct = "commitProduct";
    
    public static String c_XMLE_competency = "competency";
    public static String c_XMLE_uuid = "uuid";
    public static String c_XMLE_area = "area";
    public static String c_XMLE_index = "index";
    public static String c_XMLE_fluency = "fluency";
    public static String c_XMLE_contribution = "contribution";
    public static String c_XMLE_effectiveness = "effectiveness";
    public static String c_XMLE_recency = "recency";
    public static String c_XMLE_identity = "identity";
    
    public static String c_XMLE_identities = "identities";
    public static String c_XMLE_is = "is";
    public static String c_XMLE_isnt = "isnt";
    public static String c_XMLE_add = "add";
    public static String c_XMLE_remove = "remove";
    public static String c_XMLE_allPerson = "allPerson";
    public static String c_XMLE_isRemoved = "isRemoved";
    public static String c_XMLE_isntRemoved = "isntRemoved";
    
    public static String c_XMLE_emailSubject = "emailSubject";
    public static String c_XMLE_emailFrom = "emailFrom";
    public static String c_XMLE_emailDate = "emailDate";
    
    public static String c_XMLE_relationLevel = "relationLevel";
    
    public static String c_XMLE_concepts = "concepts";
    public static String c_XMLE_newRDFData = "newRDFData";
    public static String c_XMLE_RDF = "RDF";
    public static String c_XMLE_Description = "Description";
    public static String c_XMLE_label = "label";
    public static String c_XMLE_sameAs = "sameAs";
    public static String c_XMLE_linksTo = "linksTo";
    public static String c_XMLE_subClassOf = "subClassOf";
    public static String c_XMLE_superClassOf = "superClassOf";
       
    public static String c_XMLEA_about = "about";
    public static String c_XMLEA_resource = "resource";
    
    // </editor-fold>
    
    // <editor-fold desc="XML values">
    
    public static String c_XMLV_metadataservice = "METADATASERVICE";
    public static String c_XMLV_metadataserviceaddress = "http://www.alert-project.eu/metadata";
    public static String c_XMLV_request = "request";
    public static String c_XMLV_reply = "reply";
    public static String c_XMLV_OntClass = "OntClass";
    public static String c_XMLV_MemberURL = "MemberURL";
    
    public static String c_XMLV_sparql = "sparql";
    public static String c_XMLV_ontModelSpec = "ontModelSpec";
    public static String c_XMLV_productUri = "productUri";
    public static String c_XMLV_productID = "productID";
    public static String c_XMLV_methodUri = "methodUri";
    public static String c_XMLV_issueUri = "issueUri";
    public static String c_XMLV_issueID = "issueID";
    public static String c_XMLV_issueDuplicatesSPARQL = "issueDuplicatesSPARQL";
    public static String c_XMLV_keyword = "keyword";
    public static String c_XMLV_fromDate = "fromDate";
    public static String c_XMLV_commitUri = "commitUri";
    
    public static String c_XMLV_personForIssueSPARQL = "personForIssueSPARQL";
    
    public static String c_XMLV_firstName = "firstName";
    public static String c_XMLV_lastName = "lastName";
    public static String c_XMLV_email = "email";
    
    public static String c_XMLV_personUri = "personUri";
    public static String c_XMLV_conceptUri = "conceptUri";
    
    public static String c_XMLV_offset = "offset";
    public static String c_XMLV_count = "count";
    
    public static String c_XMLV_uuid = "uuid";

    // </editor-fold>
    
    // <editor-fold desc="API Call">
    
    public static String c_XMLAC_sparql = "sparql";
    public static String c_XMLAC_commit_getRelatedToIssue = "commit.getRelatedToIssue";
    public static String c_XMLAC_commit_getRelatedToKeyword = "commit.getRelatedToKeyword";
    public static String c_XMLAC_commit_getAllForProduct = "commit.getAllForProduct";
    public static String c_XMLAC_commit_getInfo = "commit.getInfo";
    public static String c_XMLAC_competency_getForPerson = "competency.getForPerson";
    public static String c_XMLAC_competency_getPersonForIssue = "competency.getPersonForIssue";
    public static String c_XMLAC_identity_getForPerson = "identity.getForPerson";
    public static String c_XMLAC_issue_getAllForIdentity = "issue.getAllForIdentity";
    public static String c_XMLAC_issue_getAllForMethod = "issue.getAllForMethod";
    public static String c_XMLAC_issue_getAllForProduct = "issue.getAllForProduct";
    public static String c_XMLAC_issue_getAnnotationStatus = "issue.getAnnotationStatus";
    public static String c_XMLAC_issue_getExplicitDuplicates = "issue.getExplicitDuplicates";
    public static String c_XMLAC_issue_getInfo = "issue.getInfo";
    public static String c_XMLAC_issue_getRelatedToIssue = "issue.getRelatedToIssue";
    public static String c_XMLAC_issue_getRelatedToKeyword = "issue.getRelatedToKeyword";
    public static String c_XMLAC_issue_getSubjectAreas = "issue.getSubjectAreas";
    public static String c_XMLAC_issue_getSubjectAreasForOpen = "issue.getSubjectAreasForOpen";
    public static String c_XMLAC_issue_getOpen = "issue.getOpen";
    public static String c_XMLAC_email_getRelatedToIssue = "email.getRelatedToIssue";
    public static String c_XMLAC_email_getRelatedToKeyword = "email.getRelatedToKeyword";
    public static String c_XMLAC_method_getAllForIdentity = "method.getAllForIdentity";
    public static String c_XMLAC_method_getRelatedCode = "method.getRelatedCode";
    public static String c_XMLAC_person_getAllForEmail = "person.getAllForEmail";
    public static String c_XMLAC_person_getInfo = "person.getInfo";
    public static String c_XMLAC_post_getRelatedToIssue = "post.getRelatedToIssue";
    public static String c_XMLAC_post_getRelatedToKeyword = "post.getRelatedToKeyword";
    public static String c_XMLAC_wiki_getRelatedToIssue = "wiki.getRelatedToIssue";
    public static String c_XMLAC_wiki_getRelatedToKeyword = "wiki.getRelatedToKeyword";
    public static String c_XMLAC_instance_getAllForConcept = "instance.getAllForConcept";
    public static String c_XMLAC_file_getAll = "file.getAll";
    public static String c_XMLAC_mail_getAllForProduct = "mail.getAllForProduct";
    public static String c_XMLAC_forumPost_getAllForProduct = "forumPost.getAllForProduct";
    
    // </editor-fold>
    
    // <editor-fold desc="Ontology Class">
    
    public static String c_OWLClass_Issue = "Issue";
    public static String c_OWLClass_Bug = "Bug";
    public static String c_OWLClass_Person = "Person";
    public static String c_OWLClass_Milestone = "Milestone";
    public static String c_OWLClass_Comment = "Comment";
    public static String c_OWLClass_Attachment = "Attachment";
    public static String c_OWLClass_Activity = "Activity";
    public static String c_OWLClass_ComputerSystem = "ComputerSystem";
    public static String c_OWLClass_Component = "Component";
    public static String c_OWLClass_Product = "Product";
    public static String c_OWLClass_Commit = "Commit";
    public static String c_OWLClass_IssueTracker = "IssueTracker";
    
    //Competence
    public static String c_OWLClass_Competence = "Competence";
    public static String c_OWLClass_Identity = "Identity";
    public static String c_OWLClass_Activity_Availability = "Activity_Availability";
    public static String c_OWLClass_Contribution = "Contribution";
    public static String c_OWLClass_Effectiveness = "Effectiveness";
    public static String c_OWLClass_Fluency = "Fluency";
    public static String c_OWLClass_TemporalMetric = "TemporalMetric";
    public static String c_OWLClass_APIMetric = "APIMetric";
    public static String c_OWLClass_ITSMetric = "ITSMetric";
    public static String c_OWLClass_MailingListMetric = "MailingListMetric";
    public static String c_OWLClass_SCMMetric = "SCMMetric";
    public static String c_OWLClass_StaticAnalysisMetric = "StaticAnalysisMetric";

    public static String c_OWLClass_post = "post";
    public static String c_OWLClass_threads = "threads";
    public static String c_OWLClass_forum = "forum";
    public static String c_OWLClass_Email = "Email";
    public static String c_OWLClass_WikiPage = "WikiPage";
    public static String c_OWLClass_Method = "Method";
    public static String c_OWLClass_Annotation = "Annotation";
    public static String c_OWLClass_AnnotationConcept = "AnnotationConcept";
    public static String c_OWLClass_Repository = "Repository";
    
    public static String c_OWLClass_File = "File";
    public static String c_OWLClass_Module = "Module";
    
    //action
    public static String c_OWLClass_Action = "Action";
    public static String c_OWLClass_Add = "Add";
    public static String c_OWLClass_Copy = "Copy";
    public static String c_OWLClass_Delete = "Delete";
    public static String c_OWLClass_Modify = "Modify";
    public static String c_OWLClass_Rename = "Rename";
    public static String c_OWLClass_Replace = "Replace";
    
    //state
    public static String c_OWLClass_Assigned = "Assigned";
    public static String c_OWLClass_Open = "Open";
    public static String c_OWLClass_Resolved = "Resolved";
    public static String c_OWLClass_Verified = "Verified";
    public static String c_OWLClass_Closed = "Closed";
    //resolution
    public static String c_OWLClass_Duplicate = "Duplicate";
    public static String c_OWLClass_Fixed = "Fixed";
    public static String c_OWLClass_Invalid = "Invalid";
    public static String c_OWLClass_ThirdParty = "ThirdParty";
    public static String c_OWLClass_WontFix = "WontFix";
    public static String c_OWLClass_WorksForMe = "WorksForMe";
    public static String c_OWLClass_Later = "Later";
    public static String c_OWLClass_Remind= "Remind";
    //priority
    public static String c_OWLClass_P1 = "P1";
    public static String c_OWLClass_P2 = "P2";
    public static String c_OWLClass_P3 = "P3";
    public static String c_OWLClass_P4 = "P4";
    public static String c_OWLClass_P5 = "P5";
    //priority
    public static String c_OWLClass_Blocker = "Blocker";
    public static String c_OWLClass_Critical = "Critical";
    public static String c_OWLClass_Major = "Major";
    public static String c_OWLClass_Minor = "Minor";
    public static String c_OWLClass_Trivial = "Trivial";
    
    public static String c_OWLClass_CommitEvent = "CommitEvent";
    public static String c_OWLClass_ModifiedBug = "ModifiedBug";
    public static String c_OWLClass_NewBug = "NewBug";
    public static String c_OWLClass_MailEvent = "MailEvent";
    public static String c_OWLClass_PostEvent = "PostEvent";
    public static String c_OWLClass_NewArticle = "NewArticle";
    public static String c_OWLClass_ArticleModified = "ArticleModified";
    public static String c_OWLClass_ArticleDeleted = "ArticleDeleted";
    
    // </editor-fold>
    
    // <editor-fold desc="Ontology Resource">
    
    public static String c_OWLResource_Issue = "Issue";
    
    // </editor-fold>
    
    // <editor-fold desc="Ontology Members">
    

    
    // </editor-fold>
    
    // <editor-fold desc="Ontology DataProperty">
    
    public static String c_OWLProperty_ = "";
    public static String c_OWLDataProperty_ID = "ID";
    public static String c_OWLDataProperty_URL = "URL";
    public static String c_OWLDataProperty_DateOpened = "dateOpened";
    public static String c_OWLDataProperty_Description = "description";
    public static String c_OWLDataProperty_Keyword = "keyword";
    public static String c_OWLDataProperty_IssueUrl = "issueURL";
    public static String c_OWLDataProperty_LastModified = "lastModified";
    public static String c_OWLDataProperty_Number = "number";
    public static String c_OWLDataProperty_Target = "target";
    public static String c_OWLDataProperty_Date = "date";
    public static String c_OWLDataProperty_Text = "text";
    public static String c_OWLDataProperty_FileName = "fileName";
    public static String c_OWLDataProperty_Type = "type";
    public static String c_OWLDataProperty_What = "what";
    public static String c_OWLDataProperty_Removed = "removed";
    public static String c_OWLDataProperty_Added = "added";
    public static String c_OWLDataProperty_Performed = "performed";
    public static String c_OWLDataProperty_Platform = "platform";
    public static String c_OWLDataProperty_Os = "os";
    public static String c_OWLDataProperty_Version = "version";
    public static String c_OWLDataProperty_Email = "email";
    public static String c_OWLDataProperty_FirstName = "firstName";
    public static String c_OWLDataProperty_LastName = "lastName";
    public static String c_OWLDataProperty_Username = "username";
    public static String c_OWLDataProperty_IssueTrackerType = "issueTrackerType";
    public static String c_OWLDataProperty_IssueTrackerURL = "issueTrackerURL";

    public static String c_OWLDataProperty_RevisionTag = "revisionTag";
    public static String c_OWLDataProperty_CommitDate = "commitDate";
    public static String c_OWLDataProperty_CommitMessage = "commitMessage";
    
    public static String c_OWLDataProperty_Subject = "subject";
    public static String c_OWLDataProperty_Body = "body";
    public static String c_OWLDataProperty_Category = "category";
    public static String c_OWLDataProperty_Name = "name";
    public static String c_OWLDataProperty_Uri = "uri";
    public static String c_OWLDataProperty_Weight = "weight";
    public static String c_OWLDataProperty_ForumItemID = "forumItemId";
    public static String c_OWLDataProperty_PostTime = "postTime";
    public static String c_OWLDataProperty_KeuiItemId = "keuiItemId";
    public static String c_OWLDataProperty_KeuiThreadId = "keuiThreadId";
    
    public static String c_OWLDataProperty_OnBranch = "onBranch";
    public static String c_OWLDataProperty_StartLine = "startLine";
    public static String c_OWLDataProperty_EndLine = "endLine";
    
    public static String c_OWLDataProperty_MessageId = "message-id";
    public static String c_OWLDataProperty_HasCreationDate = "origDate";
    public static String c_OWLDataProperty_Attachment = "attachment";
    
    public static String c_OWLDataProperty_HasLevel = "hasLevel";
    public static String c_OWLDataProperty_MetricName = "metricName";
    public static String c_OWLDataProperty_Time = "time";
    public static String c_OWLDataProperty_Level = "level";
    
    public static String c_OWLDataProperty_Source = "source";
    public static String c_OWLDataProperty_RawText = "rawText";
    public static String c_OWLDataProperty_WikiEditCount = "wikiEditCount";
    public static String c_OWLDataProperty_Comment = "comment";
    public static String c_OWLDataProperty_IsMinor = "isMinor";
    
    // </editor-fold>
    
    // <editor-fold desc="Ontology ObjectProperty">
    
    public static String c_OWLObjectProperty_Blocks = "blocks";
    public static String c_OWLObjectProperty_DependsOn = "dependsOn";
    public static String c_OWLObjectProperty_Fixes = "fixes";
    public static String c_OWLObjectProperty_HasReporter = "hasReporter";
    public static String c_OWLObjectProperty_HasState = "hasState";
    public static String c_OWLObjectProperty_HasResolution = "hasResolution";
    public static String c_OWLObjectProperty_InProject = "inProject";
    public static String c_OWLObjectProperty_HasComputerSystem = "hasComputerSystem";
    public static String c_OWLObjectProperty_HasPriority = "hasPriority";
    public static String c_OWLObjectProperty_HasSeverity = "hasSeverity";
    public static String c_OWLObjectProperty_HasAssignee = "hasAssignee";
    public static String c_OWLObjectProperty_HasCCPerson = "hasCcPerson";
    public static String c_OWLObjectProperty_IsDuplicateOf = "isDuplicateOf";
    public static String c_OWLObjectProperty_IsMergedInto = "isMergedInto";
    public static String c_OWLObjectProperty_HasMilestone = "hasMilestone";
    public static String c_OWLObjectProperty_HasComment = "hasComment";
    public static String c_OWLObjectProperty_HasCommentor = "hasCommentor";
    public static String c_OWLObjectProperty_HasAttachment = "hasAttachment";
    public static String c_OWLObjectProperty_HasCreator = "hasCreator";
    public static String c_OWLObjectProperty_HasActivity = "hasActivity";
    public static String c_OWLObjectProperty_HasInvolvedPerson = "hasInvolvedPerson";
    public static String c_OWLObjectProperty_IsIssueOf = "isIssueOf";
    public static String c_OWLObjectProperty_IsComponentOf = "isComponentOf";
    public static String c_OWLObjectProperty_RelatedToSourceCode = "relatedtoSourceCode";
    public static String c_OWLObjectProperty_IsIssueOfTracker = "isIssueOfTracker";
    public static String c_OWLObjectProperty_IsCommitOf = "isCommitOf";
   
    public static String c_OWLObjectProperty_IsCommitOfRepository = "isCommitOfRepository";
    public static String c_OWLObjectProperty_HasAuthor = "hasAuthor";
    public static String c_OWLObjectProperty_HasCommitter = "hasCommitter";
    
    public static String c_OWLObjectProperty_HasPosts = "hasPosts";
    public static String c_OWLObjectProperty_HasThreads = "hasThreads";
    public static String c_OWLObjectProperty_HasAnnotations = "hasAnnotations";
    public static String c_OWLObjectProperty_HasConcepts = "hasConcepts";
    public static String c_OWLObjectProperty_Author = "Author";
    public static String c_OWLObjectProperty_HasObject = "hasObject";
            
    public static String c_OWLObjectProperty_HasFile = "hasFile";
    public static String c_OWLObjectProperty_HasAction = "hasAction";
    public static String c_OWLObjectProperty_HasCommit = "hasCommit";
    public static String c_OWLObjectProperty_HasModules = "hasModules";
    public static String c_OWLObjectProperty_HasMethods = "hasMethods";
    
    public static String c_OWLObjectProperty_From = "from";
    public static String c_OWLObjectProperty_InReplyTo = "inReplyTo";
    public static String c_OWLObjectProperty_References = "references";
    
    public static String c_OWLObjectProperty_HasAttribute = "hasAttribute";
    public static String c_OWLObjectProperty_HasMetric = "hasMetric";
    public static String c_OWLObjectProperty_HasCompetences = "hasCompetences";
    
    public static String c_OWLObjectProperty_IsPerson = "isPerson";
    public static String c_OWLObjectProperty_IsntPerson = "isntPerson";
    
    public static String c_OWLObjectProperty_IsRelatedToCommit = "isRelatedToCommit";
    public static String c_OWLObjectProperty_IsRelatedToBug = "isRelatedToBug";
    public static String c_OWLObjectProperty_IsRelatedToMail = "isRelatedToMail";
    public static String c_OWLObjectProperty_IsRelatedToPost = "isRelatedToPost";
    public static String c_OWLObjectProperty_IsRelatedToWikiArticle = "isRelatedToWikiArticle";
    
    public static String c_OWLObjectProperty_HasReferenceTo = "hasReferenceTo";
    
    public static String c_OWLObjectProperty_HasContributors = "hasContributors";

    // </editor-fold>
            
    // <editor-fold desc="Ontology AnnotationProperty">
    
    public static String c_OWLAnnotationProperty_comment = "http://www.w3.org/2000/01/rdf-schema#comment";
    public static String c_OWLAnnotationProperty_apDescription = "apDescription";
    public static String c_OWLAnnotationProperty_apComment = "apComment";
    public static String c_OWLAnnotationProperty_apCommit = "apCommit";
    public static String c_OWLAnnotationProperty_apSubject = "apSubject";
    public static String c_OWLAnnotationProperty_apBody = "apBody";
    public static String c_OWLAnnotationProperty_apContent = "apContent";
    public static String c_OWLAnnotationProperty_apKeyword = "apKeyword";
    
    public static String c_OWLAnnotationProperty_Label = "label";
    public static String c_OWLAnnotationProperty_Comment = "comment";
    public static String c_OWLAnnotationProperty_SameAs = "sameAs";
    public static String c_OWLAnnotationProperty_LinksTo = "linksTo";
    
    
    // </editor-fold>
    
    public static ArrayList <String> c_Topics = new ArrayList<String>();
        
    // <editor-fold desc="NameSpace">
    
    public static String c_NS_Ifi = "http://www.ifi.uzh.ch/ddis/evoont/2008/11/bom#";
    public static String c_NS_Alert = "http://www.alert-project.eu/ontologies/alert.owl#";
    public static String c_NS_Alert_Its = "http://www.alert-project.eu/ontologies/alert_its.owl#";
    public static String c_NS_Alert_Scm = "http://www.alert-project.eu/ontologies/alert_scm.owl#";
    public static String c_NS_foaf = "http://xmlns.com/foaf/0.1/";
    public static String c_NS_doap = "http://usefulinc.com/ns/doap#";
    public static String c_NS_purl = "http://purl.org/dc/terms/";
    public static String c_NS_w3_rdf_syntax = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static String c_NS_w3_rdf_schema = "http://www.w3.org/2000/01/rdf-schema#";
    public static String c_NS_w3_flow = "http://www.w3.org/2005/01/wf/flow#";
    public static String c_NS_w3_owl = "http://www.w3.org/2002/27/owl#";
    public static String c_NS_ijs_predicate = "http://ailab.ijs.si/alert/predicate/";
    public static String c_NS_icep = "http://icep.fzi.de/icepEvents#";
    
    public static OntModel omModel = null;
    public static OntModel omAnnotation = null;
    public static Integer iBackupEventNumber = 0;
    public static Integer iBackupEventNumberLimit = 300;
    public static boolean bSilentMode = false;
    public static boolean bOnlyOutputEventLog = false;
    public static boolean bBackupProcedure = false;
    
    public static boolean bStoreOutputEventsInSeparateFolders = true;
    public static String sOutputFolderName = "Events";

    // </editor-fold>
    
    // <editor-fold desc="Constants">
    
    /** 
     * @summary Enum for person role
     * @startRealisation Sasa Stojanovic 04.02.2012.
     * @finalModification Sasa Stojanovic 04.02.2012.
     */
    public static enum PersonRole {
        CommunityManagerFacilitator,
        Designer,
        DesignerArtworkDeveloper,
        Developer,
        DistributionReleaseDeveloper,
        DocumentationWriter,
        Leader,
        Maintainer,
        ProjectLeader,
        SoftwareAnalyst,
        Tester,
        Translator,
        User,
        UserExperienceDeveloperManager
    }
    // </editor-fold>
    
    // </editor-fold>
}
