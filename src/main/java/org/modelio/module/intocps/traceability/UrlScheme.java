package org.modelio.module.intocps.traceability;

/**
 * Created by kel on 04/11/16.
 */
public class UrlScheme
{
	public enum SchemeType
	{
		/*
		 * https://github.com/overturetool/vdm2c/blob/18c8de3d9302410a9f152bca05b8ea553ef6e890/c/pom.xml
		 */
		github, /*
				 * https://gitlab.au.dk/into-cps/fmu-tester/raw/b0d9d705aa0451e61cd646ab1ed7f6baa70c0119/src/fmi2.h
				 */
		gitlab, /*
				 * The URIs have the following structure:<br/> 1. The URI representing a subpart an entity. Here <entity
				 * type> can be something like requirement, requirementSource, .. Entity.<entity type>:<git relative
				 * path>:<subpart name>#<githash of the document><br/> 2. The URI for an entity:<br/> Entity.<entity
				 * type>:<git relative path>#<githash of the document><br/> 3. The URI for an entity representing a
				 * tool:<br/> Entity.softwareTool:<toolname>:<tool version><br/> 4. The URI for an activity:<br/>
				 * Activity.<activity type>:<time in format yyyy-mm-dd-hh-mm-ss>#<unique identier for this activity such
				 * as a guid containing unique username, time (maybe computer ...)><br/> 5. The URI for an agent:<br/>
				 * Agent:<unique username><br/>
				 */
		intocps,
		/*
		 * git://<host>/path/to/git/repo?h=devel&p=src/Makefile
		 * http://www.goebel-consult.de/blog/in-need-for-an-enhanced-git-url-scheme
		 */custom
	}
}
