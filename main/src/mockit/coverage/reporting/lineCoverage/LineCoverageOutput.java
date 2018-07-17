/*
 * Copyright (c) 2006 JMockit developers
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.coverage.reporting.lineCoverage;

import java.io.*;
import java.util.List;
import javax.annotation.*;

import mockit.coverage.CallPoint;
import mockit.coverage.lines.*;
import mockit.coverage.reporting.parsing.*;

public final class LineCoverageOutput
{
   @Nonnull private final PrintWriter output;
   @Nonnull private final PerFileLineCoverage lineCoverageData;
   @Nonnull private final LineCoverageFormatter lineCoverageFormatter;

   public LineCoverageOutput(@Nonnull PrintWriter output, @Nonnull PerFileLineCoverage lineCoverageData, boolean withCallPoints) {
      this.output = output;
      this.lineCoverageData = lineCoverageData;
      lineCoverageFormatter = new LineCoverageFormatter(withCallPoints);
   }

   public boolean writeLineWithCoverageInfo(@Nonnull LineParser lineParser) {
      int line = lineParser.getNumber();

      if (!lineCoverageData.hasLineData(line)) {
         return false;
      }

      int lineExecutionCount = lineCoverageData.getExecutionCount(line);

      LineCoverageData lineData = lineCoverageData.getLineData(line);
      List<CallPoint> callPoints = lineData.getCallPoints();

      int callPointCount = 0;

      if (callPoints != null)
         callPointCount = lineData.getCallPoints().size();

      if (lineExecutionCount < 0) {
         return false;
      }

      writeLineExecutionCount(lineExecutionCount);
      writeLineCallPointsCount(callPointCount);
      writeExecutableCode(lineParser);
      return true;
   }

   private void writeLineCallPointsCount(int callPointsCount) {
      output.write("<td style='display: none' class='callpoints-count'>");
      output.print(callPointsCount);
      output.println("</td>");
   }

   private void writeLineExecutionCount(int lineExecutionCount) {
      output.write("<td class='count'>");
      output.print(lineExecutionCount);
      output.println("</td>");
   }

   private void writeExecutableCode(@Nonnull LineParser lineParser) {
      String formattedLine = lineCoverageFormatter.format(lineParser, lineCoverageData);
      output.write("      <td>");
      output.write(formattedLine);
      output.println("</td>");
   }
}
