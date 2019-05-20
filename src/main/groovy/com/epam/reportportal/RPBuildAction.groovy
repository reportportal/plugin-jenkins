/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/plugin-jenkins
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.reportportal

import hudson.model.AbstractBuild
import hudson.model.BuildBadgeAction

class RPBuildAction implements BuildBadgeAction {
    final AbstractBuild<?, ?> build
    Launch launch

    RPBuildAction(AbstractBuild<?, ?> build, Launch launch) {
        this.build = build
        this.launch = launch
    }

    @Override
    String getIconFileName() {
        '/plugin/reportportal/img/RP_ic_100.svg'
    }

    @Override
    String getDisplayName() {
        'Report Portal'
    }

    @Override
    String getUrlName() {
        'rp'
    }
}
