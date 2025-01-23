package kr.or.komca.foundation.jwt.global.utils.ip;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 클라이언트 IP 주소 관련 유틸리티 클래스
 * 프록시, 로드밸런서 등 다양한 네트워크 환경에서 실제 클라이언트 IP를 추출하고 검증
 *
 * <p>주요 기능:</p>
 * <ul>
 *   <li>다양한 HTTP 헤더를 통한 실제 클라이언트 IP 추출</li>
 *   <li>IP 변경의 위험도 평가</li>
 *   <li>모바일/데스크톱 환경 별 차별화된 IP 검증</li>
 * </ul>
 */
@Slf4j
@Component
public class ClientIpUtil {

    /**
     * IP 주소 추출을 위한 HTTP 헤더 우선순위 목록
     * 프록시 서버, 로드밸런서 등 다양한 인프라 환경 고려
     */
    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",     // 일반적인 프록시/로드밸런서
            "Proxy-Client-IP",     // 일부 프록시 서버
            "WL-Proxy-Client-IP",  // WebLogic
            "HTTP_X_FORWARDED_FOR", // 일부 프록시 서버
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"          // 최후의 수단
    };

    /**
     * 실제 클라이언트 IP 주소를 추출
     * 다양한 헤더를 순차적으로 확인하여 가장 신뢰할 수 있는 IP 주소를 반환
     *
     * @param request HTTP 요청 객체
     * @return 식별된 클라이언트 IP 주소
     */
    public String getClientIp(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String value = request.getHeader(header);
            if (isValidIpHeader(value)) {
                String[] parts = value.split("\\s*,\\s*");
                String clientIp = parts[0];
                log.debug("Client IP found in header {}: {}", header, clientIp);
                return clientIp;
            }
        }

        String remoteAddr = request.getRemoteAddr();
        log.debug("Using remote address as client IP: {}", remoteAddr);
        return remoteAddr;
    }

    /**
     * IP 변경이 보안상 의심스러운 수준인지 평가
     * 디바이스 종류와 사용 환경에 따라 다른 기준 적용
     *
     * @param originalIp 최초 접속 시 IP 주소
     * @param currentIp 현재 요청의 IP 주소
     * @param userAgent User-Agent 헤더 값
     * @param deviceInfo 디바이스 정보 (모바일/데스크톱 등)
     * @return 의심스러운 IP 변경인 경우 true
     */
    public boolean isSuspiciousIpChange(String originalIp, String currentIp,
                                        String userAgent, String deviceInfo) {
        if (originalIp == null || currentIp == null) {
            log.warn("Missing IP information - original: {}, current: {}", originalIp, currentIp);
            return true;
        }

        boolean isMobileDevice = isMobileDevice(userAgent, deviceInfo);
        log.debug("Checking IP change - original: {}, current: {}, mobile: {}",
                originalIp, currentIp, isMobileDevice);

        if (isMobileDevice) {
            boolean sameClass = isSameIpClass(originalIp, currentIp);
            log.debug("Mobile device IP class comparison result: {}", sameClass);
            return !sameClass;
        }

        // PC 환경에서는 정확한 IP 일치 요구
        boolean exactMatch = originalIp.equals(currentIp);
        log.debug("Desktop IP exact match result: {}", exactMatch);
        return !exactMatch;
    }

    /**
     * 헤더 값이 유효한 IP 정보를 포함하는지 검증
     *
     * @param headerValue 헤더 값
     * @return 유효한 IP 정보 포함 여부
     */
    private boolean isValidIpHeader(String headerValue) {
        return headerValue != null &&
                !headerValue.isEmpty() &&
                !"unknown".equalsIgnoreCase(headerValue);
    }

    /**
     * User-Agent와 디바이스 정보를 기반으로 모바일 기기 여부 판단
     *
     * @param userAgent User-Agent 헤더 값
     * @param platform 디바이스 platform
     * @return 모바일 기기인 경우 true
     */
    private boolean isMobileDevice(String userAgent, String platform) {
        boolean isMobile = false;
        // X-Device-Platform 헤더 값 확인
        isMobile = "MOBILE".equals(platform);

        if (!isMobile) {
            // User-Agent 문자열 분석
            isMobile = userAgent.contains("Mobile") ||
                    userAgent.contains("Android") ||
                    userAgent.contains("iPhone") ||
                    userAgent.contains("iPad");
        }
        return isMobile;
    }

    /**
     * 두 IP 주소가 같은 서브넷에 속하는지 검사
     * Class C 네트워크 기준으로 비교 (첫 3개 옥텟이 동일한지 확인)
     *
     * @param ip1 첫 번째 IP 주소
     * @param ip2 두 번째 IP 주소
     * @return 같은 서브넷에 속하면 true
     */
    private boolean isSameIpClass(String ip1, String ip2) {
        try {
            String[] parts1 = ip1.split("\\.");
            String[] parts2 = ip2.split("\\.");

            if (parts1.length < 3 || parts2.length < 3) {
                log.warn("Invalid IP address format - ip1: {}, ip2: {}", ip1, ip2);
                return false;
            }

            boolean result = parts1[0].equals(parts2[0]) &&
                    parts1[1].equals(parts2[1]) &&
                    parts1[2].equals(parts2[2]);

            log.debug("IP class comparison - ip1: {}, ip2: {}, result: {}", ip1, ip2, result);
            return result;
        } catch (Exception e) {
            log.error("Error comparing IP addresses: {} and {}", ip1, ip2, e);
            return false;
        }
    }
}