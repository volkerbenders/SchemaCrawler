/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2017, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.tools.commandline;


import static sf.util.Utility.isBlank;

import java.util.logging.Level;

import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.InclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.tools.options.InfoLevel;
import sf.util.SchemaCrawlerLogger;
import sf.util.StringFormat;

/**
 * Parses the command-line.
 *
 * @author Sualeh Fatehi
 */
public final class SchemaCrawlerOptionsParser
  extends BaseOptionsParser<SchemaCrawlerOptions>
{

  private static final SchemaCrawlerLogger LOGGER = SchemaCrawlerLogger
    .getLogger(SchemaCrawlerOptionsParser.class.getName());

  private static final String DEFAULT_TABLE_TYPES = "TABLE,VIEW";
  private static final String DEFAULT_ROUTINE_TYPES = "PROCEDURE,FUNCTION";

  private final SchemaCrawlerOptionsBuilder optionsBuilder;

  public SchemaCrawlerOptionsParser(final Config config)
  {
    super(config);
    normalizeOptionName("title");
    normalizeOptionName("infolevel", "i");
    normalizeOptionName("schemas");
    normalizeOptionName("tabletypes");
    normalizeOptionName("tables");
    normalizeOptionName("excludecolumns");
    normalizeOptionName("synonyms");
    normalizeOptionName("sequences");
    normalizeOptionName("routinetypes");
    normalizeOptionName("routines");
    normalizeOptionName("excludeinout");
    normalizeOptionName("grepcolumns");
    normalizeOptionName("grepinout");
    normalizeOptionName("grepdef");
    normalizeOptionName("invert-match");
    normalizeOptionName("only-matching");
    normalizeOptionName("hideemptytables");
    normalizeOptionName("parents");
    normalizeOptionName("children");

    optionsBuilder = new SchemaCrawlerOptionsBuilder().fromConfig(config);
  }

  @Override
  public SchemaCrawlerOptions getOptions()
    throws SchemaCrawlerException
  {
    if (config.hasValue("title"))
    {
      optionsBuilder.title(config.getStringValue("title", ""));
      consumeOption("title");
    }

    if (config.hasValue("infolevel"))
    {
      final String infoLevel = config.getStringValue("infolevel", "standard");
      final SchemaInfoLevel schemaInfoLevel = InfoLevel
        .valueOfFromString(infoLevel).buildSchemaInfoLevel();
      optionsBuilder.withSchemaInfoLevel(schemaInfoLevel);
      consumeOption("infolevel");
    }
    else
    {
      // Default to standard infolevel
      final SchemaInfoLevel schemaInfoLevel = InfoLevel.standard
        .buildSchemaInfoLevel();
      optionsBuilder.withSchemaInfoLevel(schemaInfoLevel);
    }

    if (config.hasValue("schemas"))
    {
      final InclusionRule schemaInclusionRule = config
        .getInclusionRule("schemas");
      logOverride("schemas", schemaInclusionRule);
      optionsBuilder.includeSchemas(schemaInclusionRule);
      consumeOption("schemas");
    }
    else
    {
      LOGGER
        .log(Level.WARNING,
             "Please provide a -schemas option for efficient retrieval of database metadata");
    }

    if (config.hasValue("tabletypes"))
    {
      final String tabletypes = config.getStringValue("tabletypes",
                                                      DEFAULT_TABLE_TYPES);
      if (!isBlank(tabletypes))
      {
        optionsBuilder.tableTypes(tabletypes);
      }
      else
      {
        optionsBuilder.tableTypes((String) null);
      }
      consumeOption("tabletypes");
    }

    if (config.hasValue("tables"))
    {
      final InclusionRule tableInclusionRule = config
        .getInclusionRule("tables");
      logOverride("tables", tableInclusionRule);
      optionsBuilder.includeTables(tableInclusionRule);
      consumeOption("tables");
    }
    if (config.hasValue("excludecolumns"))
    {
      final InclusionRule columnInclusionRule = config
        .getExclusionRule("excludecolumns");
      logOverride("excludecolumns", columnInclusionRule);
      optionsBuilder.includeColumns(columnInclusionRule);
      consumeOption("excludecolumns");
    }

    if (config.hasValue("routinetypes"))
    {
      optionsBuilder.routineTypes(config.getStringValue("routinetypes",
                                                        DEFAULT_ROUTINE_TYPES));
      consumeOption("routinetypes");
    }

    if (config.hasValue("routines"))
    {
      final InclusionRule routineInclusionRule = config
        .getInclusionRule("routines");
      logOverride("routines", routineInclusionRule);
      optionsBuilder.includeRoutines(routineInclusionRule);
      consumeOption("routines");
    }
    if (config.hasValue("excludeinout"))
    {
      final InclusionRule routineColumnInclusionRule = config
        .getExclusionRule("excludeinout");
      logOverride("excludeinout", routineColumnInclusionRule);
      optionsBuilder.includeRoutineColumns(routineColumnInclusionRule);
      consumeOption("excludeinout");
    }

    if (config.hasValue("synonyms"))
    {
      final InclusionRule synonymInclusionRule = config
        .getInclusionRule("synonyms");
      logOverride("synonyms", synonymInclusionRule);
      optionsBuilder.includeSynonyms(synonymInclusionRule);
      consumeOption("synonyms");
    }

    if (config.hasValue("sequences"))
    {
      final InclusionRule sequenceInclusionRule = config
        .getInclusionRule("sequences");
      logOverride("sequences", sequenceInclusionRule);
      optionsBuilder.includeSequences(sequenceInclusionRule);
      consumeOption("sequences");
    }

    if (config.hasValue("invert-match"))
    {
      optionsBuilder
        .invertGrepMatch(config.getBooleanValue("invert-match", true));
      consumeOption("invert-match");
    }

    if (config.hasValue("only-matching"))
    {
      optionsBuilder
        .grepOnlyMatching(config.getBooleanValue("only-matching", true));
      consumeOption("only-matching");
    }

    if (config.hasValue("grepcolumns"))
    {
      final InclusionRule grepColumnInclusionRule = config
        .getInclusionRule("grepcolumns");
      optionsBuilder.includeGreppedColumns(grepColumnInclusionRule);
      consumeOption("grepcolumns");
    }
    else
    {
      optionsBuilder.includeGreppedColumns(null);
    }

    if (config.hasValue("grepinout"))
    {
      final InclusionRule grepRoutineColumnInclusionRule = config
        .getInclusionRule("grepinout");
      optionsBuilder
        .includeGreppedRoutineColumns(grepRoutineColumnInclusionRule);
      consumeOption("grepinout");
    }
    else
    {
      optionsBuilder.includeGreppedRoutineColumns(null);
    }

    if (config.hasValue("grepdef"))
    {
      final InclusionRule grepDefinitionInclusionRule = config
        .getInclusionRule("grepdef");
      optionsBuilder.includeGreppedDefinitions(grepDefinitionInclusionRule);
      consumeOption("grepdef");
    }
    else
    {
      optionsBuilder.includeGreppedDefinitions(null);
    }

    if (config.hasValue("hideemptytables"))
    {
      final boolean hideEmptyTables = config.getBooleanValue("hideemptytables",
                                                             true);
      if (hideEmptyTables)
      {
        optionsBuilder.hideEmptyTables();
      }
      consumeOption("hideemptytables");
    }

    if (config.hasValue("parents"))
    {
      final int parentTableFilterDepth = config.getIntegerValue("parents", 0);
      optionsBuilder.parentTableFilterDepth(parentTableFilterDepth);
      consumeOption("parents");
    }
    else
    {
      optionsBuilder.parentTableFilterDepth(0);
    }

    if (config.hasValue("children"))
    {
      final int childTableFilterDepth = config.getIntegerValue("children", 0);
      optionsBuilder.childTableFilterDepth(childTableFilterDepth);
      consumeOption("children");
    }
    else
    {
      optionsBuilder.childTableFilterDepth(0);
    }

    return optionsBuilder.toOptions();
  }

  private void logOverride(final String inclusionRuleName,
                           final InclusionRule schemaInclusionRule)
  {
    LOGGER
      .log(Level.INFO,
           new StringFormat("Overriding %s inclusion rule from command-line to %s",
                            inclusionRuleName,
                            schemaInclusionRule));
  }

}
