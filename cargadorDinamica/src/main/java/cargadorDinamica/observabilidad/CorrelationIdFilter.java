package cargadorDinamica.observabilidad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);
    private static final String HEADER_NAME = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.nanoTime();
        MetricasCargadorDinamico.incRequests();

        String uri = request.getRequestURI();
        boolean esObtenerHechos = uri != null && uri.contains("/obtenerHechos");
        boolean esReportarHecho = uri != null && uri.contains("/reportarHecho");

        try {
            String correlationId = request.getHeader(HEADER_NAME);
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            MDC.put("correlationId", correlationId);
            response.setHeader(HEADER_NAME, correlationId);

            log.info("REQ CargadorDinamico correlationId={} method={} uri={}",
                    correlationId, request.getMethod(), request.getRequestURI());

            if (esObtenerHechos) {
                MetricasCargadorDinamico.incRequestsObtenerHechos();
            }

            if (esReportarHecho) {
                MetricasCargadorDinamico.incRequestsObtenerHechos();
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            MetricasCargadorDinamico.incErrors();
            throw e;
        } finally {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            MetricasCargadorDinamico.addTime(durationMs);
            MDC.clear();
        }
    }
}