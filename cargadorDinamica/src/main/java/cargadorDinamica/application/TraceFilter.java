package cargadorDinamica.application;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String traceId = request.getHeader("X-Trace-Id");
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().substring(0, 8);
        }

        String spanId = request.getHeader("X-Span-Id"); // opcional
        String correlationId = request.getHeader("X-Correlation-Id"); // si lo mandás desde core

        MDC.put("traceId", traceId);
        if (spanId != null && !spanId.isBlank()) MDC.put("spanId", spanId);
        if (correlationId != null && !correlationId.isBlank()) MDC.put("correlationId", correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
