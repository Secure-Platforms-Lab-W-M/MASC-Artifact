/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.disttest.charting.chartbuilder;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.Shape;

import org.jfree.chart.JFreeChart;

/**
 * Applies the supplied stroke and color to a series in the target chart.
 * Multiple implementations exist to because of the various chart types.
 */
public interface SeriesStrokeAndPaintApplier
{
    void setSeriesStroke(int seriesIndex, Stroke stroke, JFreeChart targetChart);
    void setSeriesPaint(int seriesIndex, Color color, JFreeChart targetChart);
    void setSeriesShape(int seriesIndex, Shape shape, JFreeChart targetChart);
}
