#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 FocusCenter;
uniform int IntoxicationLevel;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);
    float radius = float(IntoxicationLevel) + 1;

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
    float dist = (texCoord.x - FocusCenter.x) * (texCoord.x - FocusCenter.x) + (texCoord.y - FocusCenter.y) * (texCoord.y - FocusCenter.y);
    float clampedDist = clamp(dist / effectRadius, 0.0, 1.0);

    fragColor = mix(base, vec4(blur.rgb, base.a), clampedDist);
}