package org.govwatcher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MaterializedViewService {
    private final JdbcTemplate jdbcTemplate;

    public void refreshLegislativeNoticeMat() {
        try {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY legislative_notices_mat");
        } catch (Exception e) {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW legislative_notices_mat");
        }
    }

    public void refreshLegislativeNoticeDetailMat() {
        try {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY legislative_notice_detail_mat");
        } catch (Exception e) {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW legislative_notice_detail_mat");
        }
    }

    public void refreshPoliticianMat() {
        try {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY politicians_mat");
        } catch (Exception e) {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW politicians_mat");
        }
    }

    public void refreshPoliticianDetailMat() {
        try {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY politician_detail_mat");
        } catch (Exception e) {
            jdbcTemplate.execute("REFRESH MATERIALIZED VIEW politician_detail_mat");
        }
    }
}
