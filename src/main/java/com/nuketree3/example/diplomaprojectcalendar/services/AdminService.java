package com.nuketree3.example.diplomaprojectcalendar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * executeArbitrarySQL - метод, который отправляет в базу данный запрос администратора и возвращает ответ
     * @param sqlQuery - текстовый запрос администратора в базу данных
     * @return - возвращает сообщение из базы данных
     */
    public String executeArbitrarySQL(String sqlQuery) {
        if (sqlQuery == null) {
            throw new IllegalArgumentException("Недопустимый SQL-запрос.");
        }
        try {
            StringBuilder output = new StringBuilder();
            try {
                if(!sqlQuery.split(" ")[0].toUpperCase().startsWith("SELECT")) {
                    jdbcTemplate.update(sqlQuery);
                }
                else {
                    List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sqlQuery);
                    output.append(mapList);
                }
            }
            catch (DataAccessException e) {
                output.append("Ошибка доступа к данным: ").append(e.getMessage());
            }
            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка выполнения SQL: " + e.getMessage(), e);
        }
    }
}
