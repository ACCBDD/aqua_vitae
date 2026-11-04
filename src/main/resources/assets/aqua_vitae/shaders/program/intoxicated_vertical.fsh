#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 FocusCenter;
uniform int PlayerBAC;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);

    float effectRadius = 0.1;
    vec2 dir = texCoord - FocusCenter;
    float dist = dot(dir, dir);
    float clampedDist = clamp(dist / effectRadius, 0.0, 1.0);

    float radius = float(PlayerBAC / 40000) * clampedDist;

    vec4 blur = base;

    if (radius > 1.0) {
        float texelSizeY = 1.0 / InSize.y;
        vec4 colorAcc = vec4(0.0);
        float totalWeight = 0.0;

        for (float y = -radius; y <= radius; y += 1.0) {
            colorAcc += texture(DiffuseSampler, vec2(texCoord.x, texCoord.y + (y * texelSizeY)));
            totalWeight += 1.0;
        }
        blur = colorAcc / totalWeight;
    }

    vec4 focalResult = mix(base, vec4(blur.rgb, base.a), clampedDist);
    fragColor = focalResult;

    float aberrationFactor = clamp((float(PlayerBAC) - 130000) / 10000000.0, 0.0, 0.005);
    if (aberrationFactor > 0.0) {
        vec2 offset = normalize(dir) * aberrationFactor * clampedDist;

        float redChannel = texture(DiffuseSampler, clamp(texCoord + offset, vec2(0.0), vec2(1.0))).r;
        float greenChannel = focalResult.g;
        float blueChannel = texture(DiffuseSampler, clamp(texCoord - offset, vec2(0.0), vec2(1.0))).b;

        fragColor = vec4(redChannel, greenChannel, blueChannel, focalResult.a);
    }
}
