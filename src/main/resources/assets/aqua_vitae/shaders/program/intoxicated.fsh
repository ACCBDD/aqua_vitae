#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 FocusCenter;
uniform int PlayerBAC;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);
    float radius = float(PlayerBAC / 40000);

    if (radius <= 0.0) {
        fragColor = base;
        return;
    }

    vec2 texelSize = 1.0 / InSize;
    vec4 colorAcc = vec4(0.0);
    float totalWeight = 0.0;

    for (float x = -radius; x <= radius; x += 1.0) {
        for (float y = -radius; y <= radius; y += 1.0) {
            vec2 offset = vec2(x, y) * texelSize;
            colorAcc += texture(DiffuseSampler, texCoord + offset);
            totalWeight += 1.0;
        }
    }
    vec4 blur = colorAcc / totalWeight;
    float effectRadius = 0.1;
    vec2 dir = texCoord - FocusCenter;
    float dist = dot(dir, dir);
    float clampedDist = clamp(dist / effectRadius, 0.0, 1.0);

    vec4 focalResult = mix(base, vec4(blur.rgb, base.a), clampedDist);
    fragColor = focalResult;
    float aberrationFactor = clamp((float(PlayerBAC) - 130000) / 10000000, 0, 0.005);
    if (aberrationFactor > 0) {
        vec2 offset = normalize(dir) * aberrationFactor * clampedDist;

        float redChannel = texture(DiffuseSampler, clamp(texCoord + offset, vec2(0.0), vec2(1.0))).r;
        float greenChannel = focalResult.g;
        float blueChannel = texture(DiffuseSampler, clamp(texCoord - offset, vec2(0.0), vec2(1.0))).b;

        fragColor = vec4(redChannel, greenChannel, blueChannel, focalResult.a);
    }
}