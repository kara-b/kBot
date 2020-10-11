package nl.dyonb.kbot.util.command;

import discord4j.rest.util.PermissionSet;

import java.util.List;

public class CommandInfo {
    private final List<String> names;
    private final String description;
    private final boolean isEnabled;
    private final PermissionSet botPermissions;
    private final PermissionSet userPermissions;
    private final boolean botOwnerOnly;

    public CommandInfo(List<String> names, String description, boolean isEnabled, PermissionSet botPermissions, PermissionSet userPermissions, boolean botOwnerOnly) {
        this.names = names;
        this.description = description;
        this.isEnabled = isEnabled;
        this.botPermissions = botPermissions;
        this.userPermissions = userPermissions;
        this.botOwnerOnly = botOwnerOnly;
    }

    public List<String> getNames() {
        return names;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public PermissionSet getBotPermissions() {
        return botPermissions;
    }

    public PermissionSet getUserPermissions() {
        return userPermissions;
    }

    public boolean isBotOwnerOnly() {
        return botOwnerOnly;
    }

    public static class Builder {
        private Class<? extends BaseCommand> commandClass;
        private List<String> names;
        private String description;
        private boolean isEnabled;
        private PermissionSet botPermissions;
        private PermissionSet userPermissions;
        private boolean botOwnerOnly;

        public Builder(Class<? extends BaseCommand> commandClass) {
            this.commandClass = commandClass;
            this.names = List.of("");
            this.description = "";
            this.isEnabled = true;
            this.botPermissions = PermissionSet.none();
            this.userPermissions = PermissionSet.none();
            this.botOwnerOnly = false;
        }

        public Builder setNames(List<String> names) {
            this.names = names;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setIsEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        public Builder setBotPermissions(PermissionSet botPermissions) {
            this.botPermissions = botPermissions;
            return this;
        }

        public Builder setUserPermissions(PermissionSet userPermissions) {
            this.userPermissions = userPermissions;
            return this;
        }

        public Builder setPermissions(PermissionSet permissions) {
            this.botPermissions = permissions;
            this.userPermissions = permissions;
            return this;
        }

        public Builder setBotOwnerOnly(boolean botOwnerOnly) {
            this.botOwnerOnly = botOwnerOnly;
            return this;
        }

        public CommandInfo createCommandInfo() {
            return new CommandInfo(names, description, isEnabled, botPermissions, userPermissions, botOwnerOnly);
        }
    }
}
