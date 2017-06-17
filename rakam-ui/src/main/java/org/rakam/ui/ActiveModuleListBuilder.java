package org.rakam.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import org.rakam.analysis.datasource.CustomDataSourceConfig;
import org.rakam.config.ProjectConfig;
import org.rakam.report.eventexplorer.EventExplorerConfig;
import org.rakam.plugin.stream.EventStreamConfig;
import org.rakam.report.realtime.RealTimeConfig;
import org.rakam.plugin.user.UserPluginConfig;
import org.rakam.plugin.user.mailbox.UserMailboxStorage;

import javax.inject.Inject;

public class ActiveModuleListBuilder {
    private final UserPluginConfig userPluginConfig;
    private final RealTimeConfig realtimeConfig;
    private final EventStreamConfig eventStreamConfig;
    private final EventExplorerConfig eventExplorerConfig;
    private final UserPluginConfig userStorage;
    private final boolean userStorageMailbox;
    private final ProjectConfig projectConfig;
    private final boolean customDataSourceEnabled;

    @Inject
    public ActiveModuleListBuilder(UserPluginConfig userPluginConfig, CustomDataSourceConfig customDataSourceConfig, Optional<UserMailboxStorage> mailboxStorage, RealTimeConfig realtimeConfig, EventStreamConfig eventStreamConfig, EventExplorerConfig eventExplorerConfig, UserPluginConfig userStorage, ProjectConfig projectConfig) {
       this.userPluginConfig = userPluginConfig;
       this.realtimeConfig = realtimeConfig;
       this.eventStreamConfig = eventStreamConfig;
       this.eventExplorerConfig = eventExplorerConfig;
       this.userStorage = userStorage;
       this.projectConfig = projectConfig;
       this.customDataSourceEnabled = customDataSourceConfig.getEnabled();
       this.userStorageMailbox = mailboxStorage.isPresent();
    }

    public ActiveModuleList build() {
        return new ActiveModuleList(userPluginConfig, userStorageMailbox, realtimeConfig,
                eventStreamConfig, eventExplorerConfig,
                userStorage, projectConfig, customDataSourceEnabled);
    }

    public static class ActiveModuleList {
        @JsonProperty
        public final String companyName;
        @JsonProperty
        public final boolean userStorage;
        @JsonProperty
        public final boolean userMailbox;
        @JsonProperty
        public final boolean funnelAnalysisEnabled;
        @JsonProperty
        public final boolean automationEnabled;
        @JsonProperty
        public final boolean abTestingEnabled;
        @JsonProperty
        public final boolean retentionAnalysisEnabled;
        @JsonProperty
        public final boolean eventExplorer;
        @JsonProperty
        public final boolean realtime;
        @JsonProperty
        public final boolean eventStream;
        @JsonProperty
        public final boolean userStorageEventFilter;
        @JsonProperty
        public final boolean customDatabaseEnabled;

        private ActiveModuleList(UserPluginConfig userPluginConfig,  boolean userStorageMailbox,
                RealTimeConfig realtimeConfig, EventStreamConfig eventStreamConfig,
                EventExplorerConfig eventExplorerConfig, UserPluginConfig userStorage,
                ProjectConfig projectConfig, boolean customDatabaseEnabled) {
            this.userStorage = userPluginConfig.getStorageModule() != null;
            this.userMailbox = userStorageMailbox;
            this.funnelAnalysisEnabled = userPluginConfig.isFunnelAnalysisEnabled();
            this.automationEnabled = userPluginConfig.getAutomationEnabled();
            this.abTestingEnabled = userPluginConfig.getAbTestingEnabled();
            this.retentionAnalysisEnabled = userPluginConfig.isRetentionAnalysisEnabled();
            this.eventExplorer = eventExplorerConfig.isEventExplorerEnabled();
            this.realtime = realtimeConfig.isRealtimeModuleEnabled();
            this.eventStream = eventStreamConfig.getEventStreamEnabled();
            this.userStorageEventFilter = userStorage.getStorageModule() != null;
            this.companyName = projectConfig.getCompanyName();
            this.customDatabaseEnabled = customDatabaseEnabled;
        }
    }
}
