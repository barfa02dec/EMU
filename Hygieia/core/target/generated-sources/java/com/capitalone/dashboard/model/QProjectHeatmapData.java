package com.capitalone.dashboard.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QProjectHeatmapData is a Querydsl query type for ProjectHeatmapData
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QProjectHeatmapData extends BeanPath<ProjectHeatmapData> {

    private static final long serialVersionUID = -279269953L;

    public static final QProjectHeatmapData projectHeatmapData = new QProjectHeatmapData("projectHeatmapData");

    public final SimplePath<ProjectHeatmapData.ArchitectureFocus> architectureFocus = createSimple("architectureFocus", ProjectHeatmapData.ArchitectureFocus.class);

    public final SimplePath<ProjectHeatmapData.AutomatedUnitTesting> automatedUnitTesting = createSimple("automatedUnitTesting", ProjectHeatmapData.AutomatedUnitTesting.class);

    public final SimplePath<ProjectHeatmapData.CodeCoverage> codeCoverage = createSimple("codeCoverage", ProjectHeatmapData.CodeCoverage.class);

    public final SimplePath<ProjectHeatmapData.ContinuousIntegration> continuousIntegration = createSimple("continuousIntegration", ProjectHeatmapData.ContinuousIntegration.class);

    public final SimplePath<ProjectHeatmapData.CustomerWSR> customerWSR = createSimple("customerWSR", ProjectHeatmapData.CustomerWSR.class);

    public final SimplePath<ProjectHeatmapData.DesignFocus> designFocus = createSimple("designFocus", ProjectHeatmapData.DesignFocus.class);

    public final SimplePath<ProjectHeatmapData.DomainKnowledge> domainKnowledge = createSimple("domainKnowledge", ProjectHeatmapData.DomainKnowledge.class);

    public final SimplePath<ProjectHeatmapData.ManualCodeReview> manualCodeReview = createSimple("manualCodeReview", ProjectHeatmapData.ManualCodeReview.class);

    public final SimplePath<ProjectHeatmapData.Metrics> metrics = createSimple("metrics", ProjectHeatmapData.Metrics.class);

    public final SimplePath<ProjectHeatmapData.PerformanceAssessment> performanceAssessment = createSimple("performanceAssessment", ProjectHeatmapData.PerformanceAssessment.class);

    public final SimplePath<ProjectHeatmapData.ProductKnowledge> productKnowledge = createSimple("productKnowledge", ProjectHeatmapData.ProductKnowledge.class);

    public final SimplePath<ProjectHeatmapData.ReleaseProcess> releaseProcess = createSimple("releaseProcess", ProjectHeatmapData.ReleaseProcess.class);

    public final SimplePath<ProjectHeatmapData.Requirements> requirements = createSimple("requirements", ProjectHeatmapData.Requirements.class);

    public final SimplePath<ProjectHeatmapData.SecurityAssessment> securityAssessment = createSimple("securityAssessment", ProjectHeatmapData.SecurityAssessment.class);

    public final SimplePath<ProjectHeatmapData.StaticCodeAnalysis> staticCodeAnalysis = createSimple("staticCodeAnalysis", ProjectHeatmapData.StaticCodeAnalysis.class);

    public final SimplePath<ProjectHeatmapData.TeamSize> teamSize = createSimple("teamSize", ProjectHeatmapData.TeamSize.class);

    public final SimplePath<ProjectHeatmapData.TestAutomation> testAutomation = createSimple("testAutomation", ProjectHeatmapData.TestAutomation.class);

    public final SimplePath<ProjectHeatmapData.TestingProcess> testingProcess = createSimple("testingProcess", ProjectHeatmapData.TestingProcess.class);

    public QProjectHeatmapData(String variable) {
        super(ProjectHeatmapData.class, forVariable(variable));
    }

    public QProjectHeatmapData(Path<? extends ProjectHeatmapData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProjectHeatmapData(PathMetadata<?> metadata) {
        super(ProjectHeatmapData.class, metadata);
    }

}

